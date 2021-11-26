package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.service.CommentService;

import java.util.List;

@RequestMapping("/feeds/{feedId}/comments")
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    // 댓글 조회
    @GetMapping()
    public List<Comment> readComments(@PathVariable Long feedId) {
        return commentService.readcomments(feedId);
    }

    // 댓글 생성
    @PostMapping()
    public String createComment(@PathVariable Long feedId, @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(feedId, requestDto);
        return "success create Comment";
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(commentId, requestDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }




}
