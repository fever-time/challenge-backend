package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.Challenge;

import java.time.format.DateTimeFormatter;

/**
 * 유저 페이지에 챌린지 반환 Dto
 */
@Getter
@Setter
public class UserChallengeResponseDto {
    private String title;
    private String startDate;
    private String endDate;
    private Long challengeId;

    public UserChallengeResponseDto(Challenge challenge) {
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = challenge.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.challengeId = challenge.getId();
    }
}
