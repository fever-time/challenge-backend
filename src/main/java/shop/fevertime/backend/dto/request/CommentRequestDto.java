package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }
}
