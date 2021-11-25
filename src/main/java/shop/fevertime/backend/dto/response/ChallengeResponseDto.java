package shop.fevertime.backend.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import shop.fevertime.backend.domain.Category;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.ChallengeCategory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeResponseDto {

    private Long challengeId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int limitPerson;
    private boolean onOff;
    private String[] categories;
    private String image;


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
            //카테고리 불러온거를 dto로 감싸서 넣어주기
        }
        this.categories = null;
    }


}
