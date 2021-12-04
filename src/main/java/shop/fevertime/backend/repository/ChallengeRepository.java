package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByTitleContaining(String title);

    List<Challenge> findAllByCategoryNameEquals(String category);

    List<Challenge> findAllByUser(User user);

}
