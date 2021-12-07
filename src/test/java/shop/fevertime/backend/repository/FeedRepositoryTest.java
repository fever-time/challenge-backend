package shop.fevertime.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedRepositoryTest {

    @Autowired
    FeedRepository feedRepository;
    @Autowired
    UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User("test1", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
        user2 = new User("test2", "test@email.com", UserRole.USER, "654321", "https://www.img.com/img");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @Order(1)
    public void save_delete() {
        // given
        Feed feed = new Feed("피드1", user1);

        // when
        feedRepository.save(feed);

        feedRepository.delete(feed);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        Feed feed1 = new Feed("피드1", user1);
        Feed feed2 = new Feed("피드2", user1);
        Feed feed3 = new Feed("피드3", user2);

        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        // when
        List<Feed> all = feedRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @Order(3)
    public void findAllByUserId() {
        // given
        Feed feed1 = new Feed("피드1", user1);
        Feed feed2 = new Feed("피드2", user1);
        Feed feed3 = new Feed("피드3", user2);

        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        // when
        List<Feed> allByUserId = feedRepository.findAllByUserId(user1.getId());

        // then
        assertThat(allByUserId.size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    public void findByIdAndUser() {
        // given
        Feed feed1 = new Feed("피드1", user1);
        Feed feed2 = new Feed("피드2", user1);
        Feed feed3 = new Feed("피드3", user2);

        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        // when
        Optional<Feed> allByUserId = feedRepository.findByIdAndUser(feed1.getId(), user1);

        // then
        assertThat(allByUserId.orElse(null)).isEqualTo(feed1);
    }

    @Test
    @Order(5)
    public void deleteByIdAndUser() {
        // given
        Feed feed1 = new Feed("피드1", user1);
        Feed feed2 = new Feed("피드2", user1);
        Feed feed3 = new Feed("피드3", user2);

        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        // when
        feedRepository.deleteByIdAndUser(feed1.getId(), user1);

        // then
        assertThat(feedRepository.findAll().size()).isEqualTo(2);

    }
}