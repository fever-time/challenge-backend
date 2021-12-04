package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.ChallengeHistory;
import shop.fevertime.backend.domain.ChallengeStatus;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChallengeHistoryResponseDto {
    private String createdDate;
    private String missionDate;
    private ChallengeStatus challengeStatus;

    public ChallengeHistoryResponseDto(ChallengeHistory challengeHistory) {
        this.createdDate = challengeHistory.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.missionDate = challengeHistory.getMissionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.challengeStatus = challengeHistory.getChallengeStatus();
    }
}
