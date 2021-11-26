package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.api.CommentApiController;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    // 댓글 조회
    public List<Comment> readcomments(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 피드가 없습니다.")
        );
        return feed.getComments();


    }

    // 댓글 생성
    @Transactional
    public String createComment(Long feedId, CommentRequestDto requestDto) {
        // 댓글 생성 - > 해당 피드에 댓글 1 2 3 생성
//        Comment comment = new Comment(requestDto);
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 피드입니다.")
        );
        Comment comment = new Comment(feed, requestDto);
        commentRepository.save(comment);
        return "success createComment in Service";
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
