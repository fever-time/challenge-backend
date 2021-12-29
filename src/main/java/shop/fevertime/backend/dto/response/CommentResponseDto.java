package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.domain.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String username;
    private String userId;
    private String contents;
    private LocalDateTime lastModifiedDate;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.userId = comment.getFeed().getUser().getKakaoId();
        this.contents = comment.getContents();
        this.lastModifiedDate = comment.getLastModifiedDate();
    }
}
