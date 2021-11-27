package shop.fevertime.backend.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.util.DeduplicationUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private List<CertificationResponseDto> certifications = new ArrayList<>();
    private int participants;

    public ChallengeResponseDto(Challenge challenge) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.description = challenge.getDescription();
        this.image = challenge.getImgLink();
        this.startDate = challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = challenge.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.limitPerson = challenge.getLimitPerson();
        this.onOff = challenge.isOnOff();
        this.category = new CategoryResponseDto(challenge.getCategory());

        List<Certification> certifications = challenge.getCertifications();
        for (Certification certification : certifications) {
            CertificationResponseDto responseDto = new CertificationResponseDto(certification);
            this.certifications.add(responseDto);
        }

        List<CertificationResponseDto> distinct = DeduplicationUtils.deduplication(this.certifications, CertificationResponseDto::getUserId);
        this.participants = distinct.size();
    }
}
