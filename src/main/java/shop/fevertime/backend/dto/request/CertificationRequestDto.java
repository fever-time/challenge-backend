package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CertificationRequestDto {
    private Long challengeId;
    private MultipartFile img;
    private String contents;
}
