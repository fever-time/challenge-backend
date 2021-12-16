package shop.fevertime.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CertificationRepository certificationRepository;


    @Test
    @DisplayName("유저 생성")
    @Order(1)
    public void create_user() {
        // given
        User user = new User("test", "test@email.com", UserRole.USER, "1234545", "https://www.img.com/img");

        // when
        userRepository.save(user);

        // then
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 삭제")
    @Order(2)
    void delete_user() {
        // given
        User user = new User("test", "test@email.com", UserRole.USER, "1234545", "https://www.img.com/img");

        // when
        userRepository.delete(user);
        List<User> users = userRepository.findAll();

        // then
        assertThat(users.size()).isEqualTo(0);

    }


    @Test
    @DisplayName("유저 전체 조회")
    @Order(3)
    public void findAll() {
        // given
        User user1 = new User("test", "test@email.com", UserRole.USER, "1234", "https://www.img.com/img");
        User user2 = new User("test", "test@email.com", UserRole.USER, "12345", "https://www.img.com/img");
        User user3 = new User("test", "test@email.com", UserRole.USER, "23432", "https://www.img.com/img");
        User user4 = new User("test", "test@email.com", UserRole.USER, "23131", "https://www.img.com/img");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        // when
        List<User> all = userRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("카카오 아이디로 유저 조회")
    @Order(4)
    void find_user_kakao() {
        // given
        User user1 = new User("test", "test@email.com", UserRole.USER, "1234", "https://www.img.com/img");
        User user2 = new User("test2", "test@email.com", UserRole.USER, "12345", "https://www.img.com/img");

        userRepository.save(user1);
        userRepository.save(user2);

        // when
        Optional<User> byKakaoId = userRepository.findByKakaoId("1111"); //가상 카카오 아이디로 유저 조회

        // then
        assertFalse(byKakaoId.isPresent()); //조회한 USER가 존재 하지 않음
    }

    @Test
    @DisplayName("유저 챌린지 인증 내역")
    @Order(5)
    void user_certi_list() {
        // give
        User user1 = new User("test1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
        User user2 = new User("test2", "test@email.com", UserRole.USER, "123458", "https://www.img.com/img");

        userRepository.save(user1);
        userRepository.save(user2);
        Category category = new Category("운동");
        categoryRepository.save(category);

        Challenge challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user2, category, ChallengeProgress.INPROGRESS);
        challengeRepository.save(challenge1);

        Certification certification = new Certification("https://www.img.com/img", "인증", user1, challenge1);
        Certification certification2 = new Certification("https://www.img.com/img", "인증", user1, challenge1);
        Certification certification3 = new Certification("https://www.img.com/img", "인증", user2, challenge1);
        certificationRepository.save(certification);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        // when
        List<User> usersCertifis = userRepository.findAllCertifiesByChallenge(challenge1);

        // then
        assertThat(usersCertifis.size()).isEqualTo(2); //challenge1에 유저 2명이  참가
        assertEquals("test1", usersCertifis.get(0).getUsername());
        assertEquals("test2", usersCertifis.get(1).getUsername()); //참가한 유저이름 각각 확인
    }
}