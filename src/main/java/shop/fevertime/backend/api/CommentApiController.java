package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.response.CommentResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 댓글 조회 API
     */
    @GetMapping("/feeds/{feedId}/comments")
    public List<CommentResponseDto> readComments(@PathVariable Long feedId) {
        return commentService.getComments(feedId);
    }

    /**
     * 댓글 생성 API
     */
    @PostMapping("/feeds/{feedId}/comment")
    public ResultResponseDto createComment(@PathVariable Long feedId,
                                           @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(feedId, requestDto, userDetails.getUser());
        return new ResultResponseDto("success", "댓글 생성되었습니다.");
    }

    /**
     * 댓글 수정 API
     */
    @PutMapping("/feeds/{feedId}/comments/{commentId}")
    public ResultResponseDto updateComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.updateComment(feedId, commentId, requestDto, userDetails.getUser());
        return new ResultResponseDto("success", "댓글 수정되었습니다.");
    }

    /**
     * 댓글 삭제 API
     */
    @DeleteMapping("/feeds/{feedId}/comments/{commentId}")
    public ResultResponseDto deleteComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(feedId, commentId, userDetails.getUser());
        return new ResultResponseDto("success", "댓글 삭제되었습니다.");
    }

    /**
     * 댓글 생성자 확인 API
     */
    @GetMapping("/user/comment/{commentId}")
    public ResultResponseDto checkCommentCreator(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.checkCommentCreator(commentId, userDetails.getUser());
    }
}
