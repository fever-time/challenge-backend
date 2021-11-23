package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.ChallengeCategory;

public interface ChallengeCategoryRepository extends JpaRepository<ChallengeCategory, Long> {
}
