package shop.fevertime.backend.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.request.UserRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.UserChallengeResponseDto;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.service.UserService;
import shop.fevertime.backend.util.LocalDateTimeUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class UserIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    UserService userService;
    @Autowired
    FeedRepository feedRepository;

    @Nested
    @DisplayName("챌린지 객체 생성")
    class CreateChallenge {

        private String title;
        private String description;
        private String imgLink;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int limitPerson;
        private LocationType locationType;
        private String address;
        private Category category;
        private User user1;
        private User user2;

        @BeforeEach
        void setup() {
            title = "제목";
            description = "내용";
            imgLink = "https://www.img.com/img";
            startDate = LocalDateTimeUtil.getLocalDateTime("2020-01-01");
            endDate = LocalDateTimeUtil.getLocalDateTime("2020-12-12");
            limitPerson = 10;
            locationType = LocationType.OFFLINE;
            address = "강남구";
            user1 = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            user2 = new User("test2", "test@email.com", UserRole.USER, "1234567", "https://www.img.com/img");
            userRepository.save(user1);
            userRepository.save(user2);

            category = new Category("운동");
            categoryRepository.save(category);

        }

        @Test
        @Order(1)
        @DisplayName("유저가 생성한 챌린지 리스트")
        void getChallenges() throws IOException {
            //given
            Challenge challenge1 = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user1, category);
            Challenge challenge2 = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user1, category);
            Challenge challenge3 = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user2, category);
            challengeRepository.save(challenge1);
            challengeRepository.save(challenge2);
            challengeRepository.save(challenge3);

            //when
            List<UserChallengeResponseDto> challenges = userService.getChallenges(user1);

            //then
            assertThat(challenges.size()).isEqualTo(2);
        }

        @Test
        @Order(2)
        @DisplayName("유저가 생성한 피드 리스트")
        void getFeeds() throws IOException {
            //given
            Feed feed1 = new Feed("첫번째 피드", user1);
            Feed feed2 = new Feed("두번째 피드", user1);
            Feed feed3 = new Feed("처음 글", user2);
            feedRepository.save(feed1);
            feedRepository.save(feed2);
            feedRepository.save(feed3);

            //when
            List<FeedResponseDto> feeds1 = userService.getFeeds(user1.getId());
            List<FeedResponseDto> feeds2 = userService.getFeeds(user2.getId());

            //then
            assertThat(feeds1.size()).isEqualTo(2);
            assertThat(feeds2.size()).isEqualTo(1);
        }

        @Test
        @Order(3)
        @DisplayName("유저 이미지 변경")
        void updateImg() throws IOException {
            //given
            byte[] content = new byte[0];
            UserRequestDto requestDto = new UserRequestDto();
            MultipartFile newImg = new MockMultipartFile("content", "newImg.png", "multipart/mixed", content);
            requestDto.setImage(newImg);

            //when
            userService.updateUserImg(user1, requestDto);

            //then
            Optional<User> byId = userRepository.findById(user1.getId());
            //이미지 변경 확인 수정 필요
//            assertThat(byId.get().getImgLink()).isEqualTo(newImg.);
        }

        @Test
        @Order(4)
        @DisplayName("유저 이름 변경")
        void updateName() throws IOException {
            //given
            UserRequestDto requestDto = new UserRequestDto();
            requestDto.setUsername("새로운이름");

            //when
            userService.updateUsername(user2, requestDto);

            //then
            Optional<User> byId = userRepository.findById(user2.getId());
            assertThat(byId.get().getUsername()).isEqualTo("새로운이름");

        }

    }
}
