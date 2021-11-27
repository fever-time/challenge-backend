package shop.fevertime.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.Category;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {

            User user = new User("test", "test@email.com", UserRole.USER, 123456L);
            em.persist(user);

            Category category1 = new Category("운동");
            Category category2 = new Category("공부");
            Category category3 = new Category("취미");
            Category category4 = new Category("습관");
            Category category5 = new Category("기타");
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);
            em.persist(category5);
        }
    }
}
