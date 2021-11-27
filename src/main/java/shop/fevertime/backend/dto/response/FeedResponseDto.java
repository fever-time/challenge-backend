package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.fevertime.backend.domain.Feed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FeedResponseDto {
    private String contents;
    private String username;
    private List<CommentResponseDto> comments = new ArrayList<>();
    private LocalDateTime lastModifiedDate;

    public FeedResponseDto(Feed feed) {
        this.contents = feed.getContents();
        this.username = feed.getUser().getUsername();
        feed.getComments().stream()
                .map(CommentResponseDto::new)
                .forEach(commentResponseDto -> this.comments.add(commentResponseDto));
        this.lastModifiedDate = feed.getLastModifiedDate();
    }

}
