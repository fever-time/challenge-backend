package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    void deleteAllByChallengeId(Long challengeId);

    List<Certification> findAllByChallengeId(Long challengeId);

    @Query("select count(distinct c.user) from Certification c where c.challenge = :challenge")
    long countDistinctUserIdByChallenge(@Param("challenge") Challenge challenge);

    List<Certification> findAllByChallengeAndUser(Challenge challenge, User user);
}
