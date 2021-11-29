package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.dto.response.CategoryResponseDto;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByTitle(String title);

    List<Challenge> findAllByCategoryNameEquals(String category);
}
