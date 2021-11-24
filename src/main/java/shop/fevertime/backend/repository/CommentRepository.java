package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}

