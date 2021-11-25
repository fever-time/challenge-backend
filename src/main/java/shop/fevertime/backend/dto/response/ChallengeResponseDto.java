package shop.fevertime.backend.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import shop.fevertime.backend.domain.Category;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.ChallengeCategory;

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
    private List<CategoryResponseDto> categories = new ArrayList<>();
    private List<CertificationResponseDto> certifications = new ArrayList<>();

    public ChallengeResponseDto(Challenge challenge){
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.description = challenge.getDescription();
        this.image = challenge.getImgLink();
        this.startDate = challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = challenge.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.limitPerson = challenge.getLimitPerson();
        this.onOff = challenge.isOnOff();
        List<ChallengeCategory> challengeCategories = challenge.getChallengeCategories();
        for (ChallengeCategory challengeCategory : challengeCategories) {
            Category category = challengeCategory.getCategory();
            CategoryResponseDto responseDto = new CategoryResponseDto(category);
            this.categories.add(responseDto);
        }
        List<Certification> certifications = challenge.getCertifications();
        for (Certification certification : certifications) {
            CertificationResponseDto responseDto = new CertificationResponseDto(certification);
            this.certifications.add(responseDto);
        }
    }


}
