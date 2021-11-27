package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
