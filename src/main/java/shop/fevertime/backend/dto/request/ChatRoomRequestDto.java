package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomRequestDto {
    private String name;

    public ChatRoomRequestDto(String name) {
        this.name = name;
    }
}
