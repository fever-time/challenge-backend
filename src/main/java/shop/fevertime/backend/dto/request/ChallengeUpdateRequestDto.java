package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ChallengeUpdateRequestDto {
    private String address;
    private MultipartFile image;
}
