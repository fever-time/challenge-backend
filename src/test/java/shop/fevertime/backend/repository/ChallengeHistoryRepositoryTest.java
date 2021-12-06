package shop.fevertime.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.*;

import java.time.LocalDateTime;
import java.util.List;

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

        challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);
        challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
    }

    @Test
    @Order(1)
    public void save_delete() {
        // given
        ChallengeHistory challengeHistory = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        // when
        challengeHistoryRepository.save(challengeHistory);

        challengeHistoryRepository.delete(challengeHistory);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        ChallengeHistory challengeHistory1 = new ChallengeHistory(user1, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory2 = new ChallengeHistory(user1, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory3 = new ChallengeHistory(user2, challenge1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);
        ChallengeHistory challengeHistory4 = new ChallengeHistory(user2, challenge2, LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChallengeStatus.JOIN);

        challengeHistoryRepository.save(challengeHistory1);
        challengeHistoryRepository.save(challengeHistory2);
        challengeHistoryRepository.save(challengeHistory3);
        challengeHistoryRepository.save(challengeHistory4);

        // when
        List<ChallengeHistory> all = challengeHistoryRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
    }
}