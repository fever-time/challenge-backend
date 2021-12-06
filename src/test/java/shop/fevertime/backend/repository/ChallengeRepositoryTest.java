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
class ChallengeRepositoryTest {

    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    private User user1;
    private User user2;
    private Category category;

    @BeforeEach
    void setup() {
        user1 = new User("test1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
        user2 = new User("test2", "test@email.com", UserRole.USER, "654321", "https://www.img.com/img");
        userRepository.save(user1);
        userRepository.save(user2);

        category = new Category("운동");
        categoryRepository.save(category);
    }

    @Test
    @Order(1)
    public void save_delete() {
        // given
        Challenge challenge = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);

        // when
        challengeRepository.save(challenge);

        challengeRepository.delete(challenge);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        Challenge challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);
        Challenge challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);
        Challenge challenge3 = new Challenge("제목3", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category);

        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        challengeRepository.save(challenge3);

        // when
        List<Challenge> all = challengeRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
    }
}