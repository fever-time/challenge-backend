package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

