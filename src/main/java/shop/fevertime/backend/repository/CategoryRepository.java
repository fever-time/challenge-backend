package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
