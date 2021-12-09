package shop.fevertime.backend.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.request.ChallengeUpdateRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.service.ChallengeService;
import shop.fevertime.backend.service.UserService;
import shop.fevertime.backend.util.LocalDateTimeUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class ChallengeIntegrationTest {

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    UserService userService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("챌린지 객체 생성")
    class CreateChallenge {

        private final byte[] content = new byte[0];
        private User user;
        private String title;
        private String description;
        private String startDate;
        private String endDate;
        private int limitPerson;
        private LocationType locationType;
        private String address;
        private String category;
        private final ChallengeRequestDto requestDto = new ChallengeRequestDto();


        @BeforeEach
        void setup() {

            Category category1 = new Category("독서");
            categoryRepository.save(category1);

            user = null;
            title = "챌린지";
            description = "챌린지내용";
            MockMultipartFile image = new MockMultipartFile("content", "img.png", "multipart/mixed", content);
            startDate = "2020-01-01";
            endDate = "2020-12-12";
            limitPerson = 10;
            locationType = LocationType.OFFLINE;
            address = "강남구";
            category = "독서";

            requestDto.setTitle(title);
            requestDto.setDescription(description);
            requestDto.setImage(image);
            requestDto.setStartDate(startDate);
            requestDto.setEndDate(endDate);
            requestDto.setLimitPerson(limitPerson);
            requestDto.setAddress(address);
            requestDto.setLocationType(locationType);
            requestDto.setCategory(category);
        }


        @Test
        @Order(1)
        @DisplayName("로그인 없이 챌린지 생성시 에러발생")
        void test1() {
            // given

            // when
            Exception exception = assertThrows(ApiRequestException.class,
                    () -> challengeService.createChallenge(requestDto, user));

            // then
            assertEquals("유저 Id 가 유효하지 않습니다.", exception.getMessage());
        }

        @Test
        @Order(2)
        @DisplayName("로그인하고 챌린지 생성")
        void test2() throws IOException {
            // given
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            userRepository.save(user);

            // when
            challengeService.createChallenge(requestDto, user);

            // then
            List<Challenge> challenge = challengeRepository.findAllByUser(user);
            assertThat(challenge.size()).isEqualTo(1);
            Challenge challenge1 = challenge.get(0);

            assertNotNull(challenge1.getId());
            assertEquals(title, challenge1.getTitle());
            assertEquals(description, challenge1.getDescription());
            assertNotNull(challenge1.getImgLink());
            assertEquals(LocalDateTimeUtil.getLocalDateTime(startDate), challenge1.getStartDate());
            assertEquals(LocalDateTimeUtil.getLocalDateTime(endDate), challenge1.getEndDate());
            assertEquals(limitPerson, challenge1.getLimitPerson());
            assertEquals(address, challenge1.getAddress());
            assertEquals(locationType, challenge1.getLocationType());
            assertEquals(category, challenge1.getCategory().getName());

        }

        @Test
        @Order(3)
        @DisplayName("챌린지 수정")
        void test3() throws IOException {
            // given
            //챌린지 생성
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            userRepository.save(user);

            challengeService.createChallenge(requestDto, user);
            List<Challenge> allByUser = challengeRepository.findAllByUser(user);

            //수정할 내용
            String newAddress = "천안시";
            MultipartFile newImage = new MockMultipartFile("content", "newImg.png", "multipart/mixed", content);

            ChallengeUpdateRequestDto requestDto = new ChallengeUpdateRequestDto();
            requestDto.setAddress(newAddress);
            requestDto.setImage(newImage);

            // 생성자 확인
            ResultResponseDto resultResponseDto = challengeService.checkChallengeCreator(allByUser.get(0).getId(), user);
            assertThat(resultResponseDto.getMsg()).isEqualTo("챌린지 생성자가 맞습니다.");

            // when
            challengeService.updateChallenge(allByUser.get(0).getId(), requestDto, user);

            // then
            Challenge challenge = (allByUser.get(0));
            assertNotNull(challenge.getId());
            assertEquals(challenge.getAddress(), newAddress);
            assertNotNull(challenge.getImgLink());
        }

        @Test
        @Order(4)
        @DisplayName("챌린지 삭제")
        void test4() throws IOException {
            // given
            //챌린지 생성
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            userRepository.save(user);

            challengeService.createChallenge(requestDto, user);
            List<Challenge> allByUser = challengeRepository.findAllByUser(user);

            // 생성자 확인
            ResultResponseDto resultResponseDto = challengeService.checkChallengeCreator(allByUser.get(0).getId(), user);
            assertThat(resultResponseDto.getMsg()).isEqualTo("챌린지 생성자가 맞습니다.");

            // when
            challengeService.deleteChallenge(allByUser.get(0).getId(), user);

            // then
            List<Challenge> allByUser2 = challengeRepository.findAllByUser(user);
            assertThat(allByUser2.size()).isEqualTo(0);

        }

        @Test
        @Order(5)
        @DisplayName("챌린지 검색")
        void test5() throws IOException {
            // given
            //챌린지 생성
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            userRepository.save(user);

            challengeService.createChallenge(requestDto, user);

            // when
            List<ChallengeResponseDto> responseDtos = challengeService.searchChallenges("챌");

            // then
            assertThat(responseDtos.size()).isEqualTo(1);
            assertThat(responseDtos.get(0).getTitle()).isEqualTo("챌린지");

        }


    }
}
