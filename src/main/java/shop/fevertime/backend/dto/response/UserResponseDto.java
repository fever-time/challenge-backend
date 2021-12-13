package shop.fevertime.backend.dto.response;

import lombok.Getter;
import shop.fevertime.backend.domain.User;

@Getter
public class UserResponseDto {
    private final String username;
    private final String imgUrl;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.imgUrl = user.getImgUrl();
    }
}
