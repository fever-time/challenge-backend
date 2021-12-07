package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;

import java.util.Optional;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findAllByUserId(Long id);

    Optional<Feed> findByIdAndUser(Long feedId, User user);

    Optional<Feed> deleteByIdAndUser(Long feedId, User user);
}
