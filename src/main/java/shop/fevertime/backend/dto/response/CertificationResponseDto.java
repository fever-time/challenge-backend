package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.Certification;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CertificationResponseDto {
    private Long certificationId;
    private String userId;
    private String imgUrl;
    private String contents;
    private String createdDate;

    public CertificationResponseDto(Certification certification) {
        this.certificationId = certification.getId();
        this.userId = certification.getUser().getUsername();
        this.imgUrl = certification.getImgUrl();
        this.contents = certification.getContents();
        this.createdDate = certification.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
