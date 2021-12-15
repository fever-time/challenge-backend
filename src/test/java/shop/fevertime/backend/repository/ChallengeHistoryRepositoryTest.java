package shop.fevertime.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.exception.ApiRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChallengeHistoryRepositoryTest {

    @Autowired
    ChallengeHistoryRepository challengeHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ChallengeRepository challengeRepository;

    private User user1;
    private User user2;
    private Category category;
    private Challenge challenge1;
    private Challenge challenge2;

    @BeforeEach
    void setup() {
        user1 = new User("test1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
        user2 = new User("test2", "test@email.com", UserRole.USER, "654321", "https://www.img.com/img");
        userRepository.save(user1);
        userRepository.save(user2);

        category = new Category("운동");
        categoryRepository.save(category);

        challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
    }

    @Test
    @DisplayName("챌린지 기록 생성")
    @Order(1)
    public void save() {
        // given
        ChallengeHistory challengeHistory = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        // when
        challengeHistoryRepository.save(challengeHistory);

        //then
        ChallengeHistory history = challengeHistoryRepository.findChallengeHistoryByChallengeStatusEquals(ChallengeStatus.JOIN, user1, challenge1).orElseThrow(
                () -> new ApiRequestException("챌린지에 참여한 내역이 없습니다.")
        );
        assertThat(history.getChallengeStatus()).isEqualTo(ChallengeStatus.JOIN);
    }

    @Test
    @DisplayName("챌린지 기록 삭제")
    @Order(2)
    public void cancel() {
        // given
        ChallengeHistory challengeHistory = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        // when
        challengeHistoryRepository.save(challengeHistory);
        challengeHistoryRepository.delete(challengeHistory);

        //then
        assertThat(challengeHistoryRepository.findChallengeHistoryByChallengeStatusEquals(ChallengeStatus.JOIN, user1, challenge1)).isEmpty();


    }

    @Test
    @DisplayName("챌린지 몇 번 참여했는지 확인")
    @Order(3)
    public void findAll() {
        // given
        ChallengeHistory challengeHistory1 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory2 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory3 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory4 = new ChallengeHistory(user2, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        challengeHistoryRepository.save(challengeHistory1);
        challengeHistoryRepository.save(challengeHistory2);
        challengeHistoryRepository.save(challengeHistory3);
        challengeHistoryRepository.save(challengeHistory4);

        // when
        List<ChallengeHistory> all = challengeHistoryRepository.findAllByChallengeAndUser(challenge1, user1);

        // then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("챌린지 참여자 수")
    @Order(4)
    void count_participants() {
        // given
        ChallengeHistory challengeHistory1 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory2 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory3 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory4 = new ChallengeHistory(user2, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        challengeHistoryRepository.save(challengeHistory1);
        challengeHistoryRepository.save(challengeHistory2);
        challengeHistoryRepository.save(challengeHistory3);
        challengeHistoryRepository.save(challengeHistory4);

        // when
        long participants = challengeHistoryRepository.countDistinctUserByChallengeAndChallengeStatus(challenge1, ChallengeStatus.JOIN);
        assertThat(participants).isEqualTo(3);
    }

    @Test
    @DisplayName("상태값에 따른 유저 챌린지 이력")
    @Order(5)
    void user_challenge_join_list() {
        // given
        ChallengeHistory challengeHistory1 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.CANCEL);
        ChallengeHistory challengeHistory2 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory3 = new ChallengeHistory(user1, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory4 = new ChallengeHistory(user2, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        challengeHistoryRepository.save(challengeHistory1);
        challengeHistoryRepository.save(challengeHistory2);
        challengeHistoryRepository.save(challengeHistory3);
        challengeHistoryRepository.save(challengeHistory4);

        // when
        List<ChallengeHistory> joinChallengeList = challengeHistoryRepository.findAllByUserAndChallengeStatus(user1, ChallengeStatus.CANCEL);

        //then
        assertThat(joinChallengeList.size()).isEqualTo(1);
    }

}