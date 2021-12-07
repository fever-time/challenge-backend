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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CertificationRepositoryTest {

    @Autowired
    CertificationRepository certificationRepository;
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
        Certification certification = new Certification("https://www.img.com/img", "인증", user1, challenge1);

        // when
        certificationRepository.save(certification);

        certificationRepository.delete(certification);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        List<Certification> all = certificationRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    @Order(3)
    public void findAllByChallengeAndUser() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증1", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증1", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증2", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증2", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        List<Certification> allByChallengeAndUser = certificationRepository.findAllByChallengeAndUser(challenge1, user1);

        // then
        assertThat(allByChallengeAndUser.size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    public void findAllByChallenge() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증1", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증1", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증2", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증2", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        List<Certification> allByChallenge = certificationRepository.findAllByChallenge(challenge1);

        // then
        assertThat(allByChallenge.size()).isEqualTo(2);
    }

    @Test
    @Order(5)
    public void findAllByChallengeId() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증1", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증1", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증2", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증2", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        List<Certification> allByChallengeId = certificationRepository.findAllByChallengeId(challenge1.getId());

        // then
        assertThat(allByChallengeId.size()).isEqualTo(2);
    }

    @Test
    @Order(6)
    public void findByIdAndUser() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증1", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증1", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증2", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증2", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        Optional<Certification> ByIdAndUser = certificationRepository.findByIdAndUser(certification1.getId(), user1);

        // then
        assertThat(ByIdAndUser.orElse(null)).isEqualTo(certification1);
    }

    @Test
    @Order(7)
    public void deleteAllByChallenge() {
        // given
        Certification certification1 = new Certification("https://www.img.com/img", "인증1", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증1", user1, challenge2);
        Certification certification3 = new Certification("https://www.img.com/img", "인증2", user2, challenge1);
        Certification certification4 = new Certification("https://www.img.com/img", "인증2", user2, challenge2);

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);

        // when
        certificationRepository.deleteAllByChallenge(challenge2);

        // then
        assertThat(certificationRepository.findAll().size()).isEqualTo(2);
    }


}