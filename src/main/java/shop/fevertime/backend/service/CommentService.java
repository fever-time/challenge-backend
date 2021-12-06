package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.response.CommentResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;

    // 댓글 조회
    public List<CommentResponseDto> getComments(Long feedId) {
        return commentRepository.findAllByFeed_Id(feedId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public void createComment(Long feedId, CommentRequestDto requestDto, User user) {
        // 댓글 생성 - > 해당 피드에 댓글 1 2 3 생성
        Feed feed = feedRepository.findByIdAndUser(feedId, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글입니다.")
        );
        Comment comment = new Comment(feed, requestDto, user);
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findByIdAndUser(commentId, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글이거나 수정 권한이 없습니다.")
        );
        comment.commentUpdate(requestDto);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {
        commentRepository.findByIdAndUser(commentId, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글이거나 삭제 권한이 없습니다.")
        );
        commentRepository.deleteById(commentId);
    }

    /**
     * 댓글 생성자 확인 API
     */
    public ResultResponseDto checkCommentCreator(Long commentId, User user) {
        boolean present = commentRepository.findByIdAndUser(commentId, user).isPresent();
        if (present) {
            return new ResultResponseDto("success", "댓글 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "댓글 생성자가 아닙니다.");

    }
}
