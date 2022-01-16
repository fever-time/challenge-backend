package shop.fevertime.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    //메세지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; //메세지 타입
    private String roomId;
    private String sender;
    private String message;
}
