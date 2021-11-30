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
    private Long feedId;
    private String contents;
    private String username;
    private List<CommentResponseDto> comments = new ArrayList<>();
    private LocalDateTime lastModifiedDate;
    private Long kakaoId;

    public FeedResponseDto(Feed feed) {
        this.feedId = feed.getId();
        this.contents = feed.getContents();
        this.username = feed.getUser().getUsername();
        this.kakaoId = feed.getUser().getKakaoId();
        feed.getComments().stream()
                .map(CommentResponseDto::new)
                .forEach(commentResponseDto -> this.comments.add(commentResponseDto));
        this.lastModifiedDate = feed.getLastModifiedDate();
    }

}
