package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakaoId);

    @Query("select distinct u from User u join fetch u.certificationList certi where certi.challenge = :challenge")
    List<User> findAllCertifiesByChallenge(@Param("challenge") Challenge challenge);
}
