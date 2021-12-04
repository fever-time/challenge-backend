package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserCertifiesResponseDto {
    private Long id;
    private String username;
    private String imgLink;
    private List<CertificationResponseDto> certifies = new ArrayList<>();

    public UserCertifiesResponseDto(User user, List<Certification> certifies) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.imgLink = user.getImgLink();

        // 유저 인증 정보
        certifies.stream()
                .map(CertificationResponseDto::new)
                .forEach(certificationResponseDto -> this.certifies.add(certificationResponseDto));
    }

}
