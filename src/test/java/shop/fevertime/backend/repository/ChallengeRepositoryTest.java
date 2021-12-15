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
    @DisplayName("챌린지 생성")
    @Order(1)
    public void save() {
        // given
        Challenge challenge = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);

        // when
        challengeRepository.save(challenge);
        challengeRepository.save(challenge2);

        // then
        List<Challenge> all = challengeRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("챌린지 삭제")
    @Order(2)
    public void delete() {
        // given
        Challenge challenge = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);

        // when
        challengeRepository.save(challenge);
        challengeRepository.save(challenge2);

        challengeRepository.delete(challenge);
        challengeRepository.delete(challenge2);

        // then
        List<Challenge> all = challengeRepository.findAll();
        assertThat(all.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("챌린지 전체 조회")
    @Order(3)
    public void findAll() {
        // given
        Challenge challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge3 = new Challenge("제목3", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);

        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        challengeRepository.save(challenge3);

        // when
        List<Challenge> all = challengeRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("챌린지 검색")
    @Order(4)
    void search_challenge() {
        // given
        Challenge challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge3 = new Challenge("제목3", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);

        // when
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        challengeRepository.save(challenge3);

        List<Challenge> searchList = challengeRepository.findAllByTitleContaining("제목");
        assertThat(searchList.size()).isEqualTo(3);


    }

    @Test
    @DisplayName("카테고리 이름으로 검색")
    @Order(5)
    void find_by_category() {
        // given
        Category category1 = new Category("취미");
        categoryRepository.save(category1);


        Challenge challenge1 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목2", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge3 = new Challenge("제목3", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category1, ChallengeProgress.INPROGRESS);
        Challenge challenge4 = new Challenge("제목3", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category1, ChallengeProgress.INPROGRESS);

        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        challengeRepository.save(challenge3);
        challengeRepository.save(challenge4);

        // when
        List<Challenge> challenges = challengeRepository.findAllByCategoryNameEquals("취미");

        // then
        assertThat(challenges.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자가 생성한 챌린지 조회")
    @Order(6)
    void findAll_by_user() {
        // given
        Challenge challenge = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        Challenge challenge2 = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        challengeRepository.save(challenge);
        challengeRepository.save(challenge2);

        // when
        List<Challenge> challenges = challengeRepository.findAllByUser(user1);

        // then
        assertThat(challenges.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("챌린지 상세보기")
    @Order(7)
    void challenge_detail() {
        // given
        Challenge challenge = new Challenge("제목1", "설명", "https://www.img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.OFFLINE, "서울", user1, category, ChallengeProgress.INPROGRESS);
        challengeRepository.save(challenge);

        // when
        Challenge challengeDetail = challengeRepository.findByIdAndUser(challenge.getId(), user1).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 챌린지입니다.")
        );

        // then
        assertThat(challengeDetail.getTitle()).isEqualTo("제목1");


    }

}