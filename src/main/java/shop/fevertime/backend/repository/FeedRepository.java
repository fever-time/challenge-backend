package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
