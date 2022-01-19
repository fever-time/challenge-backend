package shop.fevertime.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private Long roomId;
    private String sender;
    private String message;
}
