package shop.fevertime.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FeedResponseDto {
    private String contents;
    private String username;
    private List<CommentResponseDto> comments = new ArrayList<>();
    private LocalDateTime lastModifiedDate;

    public FeedResponseDto(Feed feed) {
        this.contents = feed.getContents();
//        this.username = feed.getUser().getUsername();
        for (Comment comment : feed.getComments()) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            this.comments.add(commentResponseDto);
        }
        this.lastModifiedDate = feed.getLastModifiedDate();
    }

}
