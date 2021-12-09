package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedRequestDto {
    private String contents;

    public FeedRequestDto(String contents) {
        this.contents = contents;
    }
}
