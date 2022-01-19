package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.fevertime.backend.domain.ChatRoom;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private String name;
    private String createdDate;
    private String creator;
    private int userCount;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getId();
        this.name= chatRoom.getName();
        this.createdDate = chatRoom.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.creator = chatRoom.getUser().getKakaoId();
        this.userCount = chatRoom.getUserCount();
    }

}
