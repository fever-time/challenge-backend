package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    void deleteAllByChallengeId(Long challengeId);

    List<Certification> findAllByChallengeId(Long challengeId);

    List<Certification> findAllByChallengeAndUser(Challenge challenge, User user);
}
