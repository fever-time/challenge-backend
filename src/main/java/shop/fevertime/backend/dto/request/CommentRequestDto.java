package shop.fevertime.backend.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long feedId;
    private String contents;
}
