package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Certification;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    void deleteByChallengeId(Long challengeId);

    List<Certification> findAllByChallengeId(Long challengeId);
}
