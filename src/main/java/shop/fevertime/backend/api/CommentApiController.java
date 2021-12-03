package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.response.CommentResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.CommentService;

import java.util.List;

@RequestMapping("/feeds/{feedId}")
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/comments")
    public List<CommentResponseDto> readComments(@PathVariable Long feedId) {
        return commentService.readcomments(feedId);
    }

    // 댓글 생성
    @PostMapping("/comment")
    public String createComment(@PathVariable Long feedId,
                                @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(feedId, requestDto, userDetails.getUser());
        return "ok";
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public String updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(commentId, requestDto);
        return "ok";
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "ok";
    }


}
