package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.api.CommentApiController;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> readcomments(Long feedId) {
        return commentRepository.findAllById(Collections.singleton(feedId));
    }

    @Transactional
    public Comment createComment(CommentRequestDto requestDto) {
        // 댓글 생성 - > 해당 피드에 댓글 1 2 3 생성
        Comment comment = new Comment(requestDto);

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 아이디가 없습니다.")
        );
        comment.commentUpdate(requestDto);
        return comment;
    }

    @Transactional
    public String deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return "delete success";

    }
}
