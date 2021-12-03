package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findByIdAndUser(Long feedId, User user);
}
