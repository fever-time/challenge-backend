package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import shop.fevertime.backend.domain.Challenge;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeResponseDto {

    private Long challengeId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int limitPerson;
    private boolean onOff;
    private String image;
    private CategoryResponseDto category;
    private long participants;

    public ChallengeResponseDto(Challenge challenge, long participants) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.description = challenge.getDescription();
        this.image = challenge.getImgLink();
        this.startDate = challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = challenge.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.limitPerson = challenge.getLimitPerson();
        this.onOff = challenge.isOnOff();
        this.category = new CategoryResponseDto(challenge.getCategory());
        this.participants = participants;
    }
}
