package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.User;

import java.util.List;

@Setter
@Getter
public class ChallengeUserResponseDto {
    // User
    private Long userId;
    private String username;
    private String userImgLink;

    // Certification List
    private List<CertificationResponseDto> certifies;

    // user ChallengeHistory List
    private List<ChallengeHistoryResponseDto> userHistories;

    public ChallengeUserResponseDto(
            User user,
            List<CertificationResponseDto> certifies,
            List<ChallengeHistoryResponseDto> userHistories
    ) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.userImgLink = user.getImgLink();
        this.certifies = certifies;
        this.userHistories = userHistories;
    }
}
