package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.repository.CertificationRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 유저 페이지에 챌린지 반환 Dto
 */
@Getter
@Setter
public class UserChallengeInfoDto {

    private Long challengeId; // 챌린지 식별자
    private String title; // 챌린지 타이틀
    private String imgUrl; // 챌린지 이미지
    private CategoryResponseDto category; // 챌린지 카테고리
    private LocationType locationType; // 챌린지 로컬타입
    private String challengeStartDate; // 챌린지 시작 날짜
    private String challengeEndDate; // 챌린지 종료 날짜
    private String challengeStatus; // 챌린지 상태 (JOIN, RETRY, COMPLETE)
    private int certiCount; // 인증 횟수
    private int retryCount; // 재도전 횟수
    private String userStartDate; // 챌린지 미션 시작 날짜
    private String userMissonDate; // 챌린지 미션 종료 날짜
//    private String 챌린지미션진행여부; // Y OR NO

    //    private String description;


    public UserChallengeInfoDto(Challenge challenge, ChallengeHistory challengeHistory, List<Certification> certification, List<ChallengeHistory> challengeRetry) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.imgUrl = challenge.getImgUrl();
        this.category = new CategoryResponseDto(challenge.getCategory());
        this.locationType = challenge.getLocationType();
        this.challengeStartDate = challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.challengeEndDate = challenge.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.challengeStatus = challengeHistory.getChallengeStatus().toString();
        this.certiCount = certification.size();
        this.retryCount = challengeRetry.size();
        this.userStartDate = challengeHistory.getCreatedDate().toString();
        this.userMissonDate = challengeHistory.getMissionDate().toString();
        // 챌린지 미션 진행여부


    }

}