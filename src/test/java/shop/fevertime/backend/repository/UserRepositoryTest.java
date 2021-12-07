package shop.fevertime.backend.repository;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Order(1)
    public void save_delete() {
        // given
        User test = new User("test", "test@email.com", UserRole.USER, "1234545", "https://www.img.com/img");

        // when
        userRepository.save(test);

        userRepository.delete(test);
    }

    @Test
    @Order(2)
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

}