package shop.fevertime.backend.repository;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    public void save_delete() {
        // given
        Category category1 = new Category("운동");
        Category category2 = new Category("공부");
        Category category3 = new Category("취미");

        // when
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        categoryRepository.delete(category1);
        categoryRepository.delete(category2);
        categoryRepository.delete(category3);
    }

    @Test
    @Order(2)
    public void findAll() {
        // given
        Category category1 = new Category("운동");
        Category category2 = new Category("공부");
        Category category3 = new Category("취미");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        // when
        List<Category> all = categoryRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @Order(3)
    public void findByName() {
        // given
        Category category1 = new Category("운동");
        Category category2 = new Category("공부");
        Category category3 = new Category("취미");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        // when
        Category findCategory1 = categoryRepository.findByName("운동").get();
        Category findCategory2 = categoryRepository.findByName("공부").get();
        Category findCategory3 = categoryRepository.findByName("취미").get();

        // then
        assertThat(findCategory1).isEqualTo(category1);
        assertThat(findCategory2).isEqualTo(category2);
        assertThat(findCategory3).isEqualTo(category3);
    }
}