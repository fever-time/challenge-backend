package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    void deleteAllByChallenge(Challenge challenge);

    List<Certification> findAllByChallengeId(Long challengeId);

    List<Certification> findAllByChallenge(Challenge challenge);

    List<Certification> findAllByChallengeAndUser(Challenge challenge, User user);

    Optional<Certification> findByIdAndUser(Long certificationId, User user);
}
