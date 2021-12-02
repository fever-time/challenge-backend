package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.response.CommentResponseDto;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;

    // 댓글 조회
    public List<CommentResponseDto> readcomments(Long feedId) {

        return commentRepository.findAllByFeed_Id(feedId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public void createComment(Long feedId, CommentRequestDto requestDto, User user) {
        // 댓글 생성 - > 해당 피드에 댓글 1 2 3 생성
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 피드입니다.")
        );
        Comment comment = new Comment(feed, requestDto, user);
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public Long updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 아이디가 없습니다.")
        );
        comment.commentUpdate(requestDto);
        return comment.getId();
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
