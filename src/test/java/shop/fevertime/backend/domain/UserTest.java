package shop.fevertime.backend.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class UserTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Test
    public void createUser() throws Exception {
        // given
        User user = new User("test", "test@email.com", UserRole.USER, 123456L);
        // when
        userRepository.save(user);
    }

}