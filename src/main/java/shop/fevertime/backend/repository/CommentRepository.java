package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByFeed(Feed feed);

    List<Comment> findAllByFeed(Feed feed);

    Optional<Comment> findByIdAndUser(Long commentId, User user);
}

