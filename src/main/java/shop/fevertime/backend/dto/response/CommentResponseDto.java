package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import shop.fevertime.backend.domain.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String username;
    private String contents;
    private LocalDateTime lastModifiedDate;

    public CommentResponseDto(Comment comment) {
//        BeanUtils.copyProperties(comment, this);
//        this.username = comment.getUser().getUsername();
        this.contents = comment.getContents();
        this.lastModifiedDate = comment.getLastModifiedDate();
    }
}
