package shop.fevertime.backend.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.service.CommentService;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentIntegrationTest {

    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    CommentRepository commentRepository;

    @Nested
    @DisplayName("댓글 객체 생성")
    class CreateComment {

        @Test
        @Transactional
        @DisplayName("회원 정보 없이 댓글 등록 시 예외 발생")
        void need_login() {
            // given
            User user = new User("강준호", "test@email.com", UserRole.USER, "1234567", "https://img.com/img");
            Feed feed = new Feed("첫번째 피드", user);

            userRepository.save(user);
            feedRepository.save(feed);

            // when
            Exception exception = assertThrows(ApiRequestException.class,
                    () -> new Comment(feed, "첫번째 댓글", null));

            CommentRequestDto commentRequestDto = new CommentRequestDto("첫번째 댓글 성공");

            commentService.createComment(feed.getId(), commentRequestDto, user);
            commentService.createComment(feed.getId(), commentRequestDto, user);
            commentService.createComment(feed.getId(), commentRequestDto, user);
            List<Comment> comments = commentRepository.findAll();

            // then
            assertEquals("유저 정보가 유효하지 않습니다.", exception.getMessage());
            assertThat(comments.size()).isEqualTo(3);

        }
    }

    @Test
    @Transactional
    @DisplayName("댓글 생성자가 아닌 다른 사람이 댓글 삭제 불가능")
    void check_creator_delete() {

        // given
        User user = new User("강준호", "test@email.com", UserRole.USER, "123", "https://img.com/img");
        User user2 = new User("강현규", "test1@email.com", UserRole.USER, "456", "https://img.com/img");

        userRepository.save(user);
        userRepository.save(user2);


        Feed feed = new Feed("첫번째 피드", user);
        feedRepository.save(feed);

        Comment comment1 = new Comment(feed, "첫번째 댓글", user);
        Comment comment2 = new Comment(feed, "두번째 댓글", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        Exception exception = assertThrows(ApiRequestException.class,
                () -> commentService.deleteComment(feed.getId(), comment1.getId(), user2));// 현규님이 내 댓글을 지우려고 한다.

        commentService.deleteComment(feed.getId(), comment1.getId(), user); // 본인의 댓글을 지우면
        List<Comment> comments = commentRepository.findAll(); // 댓글은 총 2개가 된다.
        // then
        assertEquals("존재하지 않는 댓글이거나 삭제 권한이 없습니다.", exception.getMessage());
        assertEquals(2, comments.size());
    }

    @Test
    @Transactional
    @DisplayName("댓글 생성자가 아닌 다른 사람이 댓글 수정 불가능")
    void check_creator_update() {
        User user = new User("강준호", "test@email.com", UserRole.USER, "123", "https://img.com/img");
        User user2 = new User("김수빈", "test2@email.com", UserRole.USER, "789", "https://img.com/img");
        userRepository.save(user);
        userRepository.save(user2);

        Feed feed = new Feed("첫번째 피드", user);
        feedRepository.save(feed);

        Comment comment1 = new Comment(feed, "첫번째 댓글", user);
        Comment comment2 = new Comment(feed, "두번째 댓글", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        CommentRequestDto commentRequestDto = new CommentRequestDto("수정 댓글");

        // when
        Exception exception = assertThrows(ApiRequestException.class,
                () -> commentService.updateComment(feed.getId(), comment1.getId(), commentRequestDto, user2));// 수빈님이 내 댓글을 수정하려고 한다.

        // then
        assertEquals("존재하지 않는 댓글이거나 수정 권한이 없습니다.", exception.getMessage());

    }
}
