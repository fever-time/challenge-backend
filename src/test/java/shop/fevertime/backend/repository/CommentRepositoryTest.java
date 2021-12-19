package shop.fevertime.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeedRepository feedRepository;

    private User user1;
    private User user2;
    private Feed feed1;
    private Feed feed2;

    @BeforeEach
    void setup() {
        user1 = new User("test1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
        user2 = new User("test2", "test@email.com", UserRole.USER, "654321", "https://www.img.com/img");
        userRepository.save(user1);
        userRepository.save(user2);

        feed1 = new Feed("feed1", user1);
        feed2 = new Feed("feed2", user2);
        feedRepository.save(feed1);
        feedRepository.save(feed2);
    }

    @Test
    @Order(1)
    public void save_delete() {
        // given
        Comment comment = new Comment(feed1, "댓글1", user1);

        // when
        commentRepository.save(comment);

        commentRepository.delete(comment);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        Comment comment1 = new Comment(feed1, "댓글1", user1);
        Comment comment2 = new Comment(feed1, "댓글2", user2);
        Comment comment3 = new Comment(feed2, "댓글1", user1);
        Comment comment4 = new Comment(feed2, "댓글2", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        // when
        List<Comment> all = commentRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    @Order(3)
    public void findAllByFeed() {
        // given
        Comment comment1 = new Comment(feed1, "댓글1", user1);
        Comment comment2 = new Comment(feed1, "댓글2", user2);
        Comment comment3 = new Comment(feed2, "댓글1", user1);
        Comment comment4 = new Comment(feed2, "댓글2", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        // when
        List<Comment> allByFeed = commentRepository.findAllByFeedAndParentIsNull(feed1);

        // then
        assertThat(allByFeed.size()).isEqualTo(2);
    }

    @Test
    @Order(3)
    public void findByIdAndUser() {
        // given
        Comment comment1 = new Comment(feed1, "댓글1", user1);
        Comment comment2 = new Comment(feed1, "댓글2", user2);
        Comment comment3 = new Comment(feed2, "댓글1", user1);
        Comment comment4 = new Comment(feed2, "댓글2", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        // when
        Comment ByIdAndUser = commentRepository.findByIdAndUser(comment1.getId(), user1).orElse(null);

        // then
        assertThat(ByIdAndUser).isEqualTo(comment1);
    }

    @Test
    @Order(4)
    public void deleteAllByFeed() {
        // given
        Comment comment1 = new Comment(feed1, "댓글1", user1);
        Comment comment2 = new Comment(feed1, "댓글2", user2);
        Comment comment3 = new Comment(feed2, "댓글1", user1);
        Comment comment4 = new Comment(feed2, "댓글2", user2);
        Comment comment5 = new Comment(feed2, "댓글3", user2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);

        // when
        commentRepository.deleteAllByFeed(feed2);

        // then
        assertThat(commentRepository.findAll().size()).isEqualTo(2);
    }

}