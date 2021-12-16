package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserRequestDto {
    private String username;
}
