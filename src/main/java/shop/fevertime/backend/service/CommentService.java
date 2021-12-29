package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.response.CommentResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;

    // 댓글 조회
    public List<CommentResponseDto> getComments(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("피드가 존재하지 않습니다.")
        );
        return commentRepository.findAllByFeedAndParentIsNull(feed)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 대댓글 조회
    public List<CommentResponseDto> getChildComments(Long feedId, Long commentId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("피드가 존재하지 않습니다.")
        );
        Comment parent = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글입니다.")
        );
        return parent.getChild()
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long feedId, CommentRequestDto requestDto, User user) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        Comment comment = new Comment(feed, requestDto.getContents(), user);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 대댓글 생성
    @Transactional
    public void createChildComment(Long feedId, Long commentId, CommentRequestDto requestDto, User user) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        Comment parent = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글입니다.")
        );
        Comment child = Comment.createChildComment(feed, requestDto.getContents(), user, parent);
        commentRepository.save(child);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long feedId, Long commentId, CommentRequestDto requestDto, User user) {
        feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        Comment comment = commentRepository.findByIdAndUser(commentId, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글이거나 수정 권한이 없습니다.")
        );
        comment.update(requestDto.getContents());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long feedId, Long commentId, User user) {
        feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        Comment comment = commentRepository.findByIdAndUser(commentId, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 댓글이거나 삭제 권한이 없습니다.")
        );
        commentRepository.delete(comment);
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
