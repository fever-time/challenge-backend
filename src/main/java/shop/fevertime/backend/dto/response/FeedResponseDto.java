package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.Feed;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedResponseDto {
    private Long feedId;
    private String contents;
    private String username;
    private String imgLink;
    private LocalDateTime lastModifiedDate;

    public FeedResponseDto(Feed feed) {
        this.feedId = feed.getId();
        this.contents = feed.getContents();
        this.username = feed.getUser().getUsername();
        this.imgLink = feed.getUser().getImgLink();
        this.lastModifiedDate = feed.getLastModifiedDate();
    }
}
