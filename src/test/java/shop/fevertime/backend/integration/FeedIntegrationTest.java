package shop.fevertime.backend.Integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.service.FeedService;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedIntegrationTest {

    @Autowired
    FeedService feedService;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("피드 객체 생성")
     class CreateFeed {
        private String contents;
        private User user;

        @BeforeEach
        void setUp() {
            contents = "첫번째 피드";
        }

        @Test
        @DisplayName("회원 정보 없이 피드 등록 시 예외 발생")
        @Transactional
        void need_login() {
            // given
            FeedRequestDto feedRequestDto = new FeedRequestDto(contents);

            // when
            Exception exception = assertThrows(ApiRequestException.class,
                    () -> feedService.createFeed(feedRequestDto, null));
            // then
            assertEquals("유저 정보가 유효하지 않습니다.", exception.getMessage());
        }


        @Test
        @Transactional
        @DisplayName("피드 수정")
        void update_feed() {
            // given
            User user = new User("사용자 이름", "test@email.com", UserRole.USER, "1234567", "https://img.com/img");
            userRepository.save(user);
            // 유저 정보를 저장 안해서 에러

            Feed feed = new Feed(contents, user);
            feedRepository.save(feed);

            FeedRequestDto updateRequestDto = new FeedRequestDto("수정 피드");

            // when
            ResultResponseDto resultResponseDto = feedService.updateFeed(feed.getId(), updateRequestDto, user);

            // then
            assertThat(resultResponseDto.getResult()).isEqualTo("success");
        }


        @Test
        @Transactional
        @DisplayName("회원 정보 없이 피드 삭제 시 예외 발생")
        void delete_feed() {
            // given
            User user = new User("사용자 이름", "test@email.com", UserRole.USER, "1234567", "https://img.com/img");
            userRepository.save(user);
            // 유저 정보를 저장 안해서 에러

            Feed feed = new Feed(contents, user);
            feedRepository.save(feed);

            // when
            Exception exception = assertThrows(ApiRequestException.class,
                    () -> feedService.deleteFeed(feed.getId(), null));
            feedService.deleteFeed(feed.getId(), user);
            List<FeedResponseDto> feeds = feedService.getFeeds();
            // then
            assertEquals("피드가 존재하지 않거나 삭제 권한이 없습니다.", exception.getMessage());
            assertThat(feeds.size()).isEqualTo(0);
        }
    }




}
