package shop.fevertime.backend.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.response.ChallengeUserResponseDto;
import shop.fevertime.backend.dto.response.UserCertifiesResponseDto;
import shop.fevertime.backend.repository.*;
import shop.fevertime.backend.service.ChallengeHistoryService;
import shop.fevertime.backend.util.LocalDateTimeUtil;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeHistoryIntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    CertificationRepository certificationRepository;
    @Autowired
    ChallengeHistoryRepository challengeHistoryRepository;
    @Autowired
    ChallengeHistoryService challengeHistoryService;

    @Nested
    @DisplayName("유저, 챌린지, 인증 객체 생성")
    class CreateObjects {

        private User user1;
        private User user2;
        private User user3;
        private Challenge challenge;

        @BeforeEach
        void setup() {
            String title = "제목";
            String description = "내용";
            String imgLink = "https://www.img.com/img";
            LocalDateTime startDate = LocalDateTimeUtil.getLocalDateTime("2020-01-01");
            LocalDateTime endDate = LocalDateTimeUtil.getLocalDateTime("2020-12-12");
            int limitPerson = 10;
            LocationType locationType = LocationType.OFFLINE;
            String address = "강남구";
            user1 = new User("user1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            user2 = new User("user2", "test@email.com", UserRole.USER, "1234567", "https://www.img.com/img");
            user3 = new User("user3", "test@email.com", UserRole.USER, "1234567", "https://www.img.com/img");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            Category category = new Category("운동");
            categoryRepository.save(category);

            challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user1, category);
            challengeRepository.save(challenge);

            Certification certification1 = new Certification(imgLink, "가인증1", user1, challenge);
            Certification certification2 = new Certification(imgLink, "가인증2", user1, challenge);
            Certification certification3 = new Certification(imgLink, "나인증1", user2, challenge);
            certificationRepository.save(certification1);
            certificationRepository.save(certification2);
            certificationRepository.save(certification3);

            //user1,user2 챌린지 참여
            ChallengeHistory challengeHistory1 = new ChallengeHistory(user1, challenge, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
            ChallengeHistory challengeHistory2 = new ChallengeHistory(user2, challenge, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
            challengeHistoryRepository.save(challengeHistory1);
            challengeHistoryRepository.save(challengeHistory2);
        }

        @Test
        @Order(1)
        @DisplayName("참여 챌린지 & 내역")
        void getChallengeHistoryUser() {
            //given

            //when
            ChallengeUserResponseDto challengeHistoryUser = challengeHistoryService.getChallengeHistoryUser(challenge.getId(), user1);

            //then
            assertEquals(challengeHistoryUser.getUsername(),"user1");
            assertThat(challengeHistoryUser.getCertifies().size()).isEqualTo(2); //챌린지 참여한 user1의 인증 횟수
            assertThat(challengeHistoryUser.getUserHistories().get(0).getChallengeStatus()).isEqualTo(ChallengeStatus.JOIN);
        }

        @Test
        @Order(2)
        @DisplayName("챌린지에 참여한 유저 리스트")
        void getChallengeHistoryUsers() {
            //given

            //when
            List<UserCertifiesResponseDto> challengeHistoryUsers = challengeHistoryService.getChallengeHistoryUsers(challenge.getId());

            //then
            assertThat(challengeHistoryUsers.size()).isEqualTo(2);//챌린지 참여 유저 인원
            assertThat(challengeHistoryUsers.get(0).getUsername()).isEqualTo("user1");
            assertThat(challengeHistoryUsers.get(1).getUsername()).isEqualTo("user2");
            assertThat(challengeHistoryUsers.get(1).getCertifies().get(0).getContents()).isEqualTo("나인증1");//유저2의 인증 내용
        }

        @Test
        @Order(3)
        @Transactional
        @DisplayName("챌린지에 참여하기")
        void joinChallenge() {
            //given

            //when
            challengeHistoryService.joinChallenge(challenge.getId(),user3);//유저3 챌린지 참여

            //then
            List<ChallengeHistory> allByChallengeAndUser = challengeHistoryRepository.findAllByChallengeAndUser(challenge, user3);
            assertThat(allByChallengeAndUser.get(0).getChallengeStatus()).isEqualTo(ChallengeStatus.JOIN); //챌린지 히스토리에 유저3 참여 상태
            assertThat(allByChallengeAndUser.get(0).getUser()).isEqualTo(user3);
        }

        @Test
        @Order(4)
        @Transactional
        @DisplayName("챌린지에 참여 취소하기")
        void cancelChallenge() {
            //given

            //when
            challengeHistoryService.cancelChallenge(challenge.getId(),user1);//유저1 챌린지 참여 취소
            //참여 안한 유저가 참여 취소
            Exception exception = assertThrows(NoSuchElementException.class,
                    () -> challengeHistoryService.cancelChallenge(challenge.getId(),user3));

            //then
            List<ChallengeHistory> user1status = challengeHistoryRepository.findAllByUserAndChallengeStatus(user1, ChallengeStatus.CANCEL);
            assertThat(user1status.size()).isEqualTo(1);//챌린지 참여 인원 ( user1이 취소해서 user2만 남음 )

            assertThat(exception.getMessage()).isEqualTo("해당 챌린지를 참여중인 기록이 없습니다."); //참여 하지 않은 user3이 챌린지 취소 했을 떄
        }

    }
}
