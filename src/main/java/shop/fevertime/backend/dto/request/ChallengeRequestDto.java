package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import shop.fevertime.backend.domain.ChallengeProgress;
import shop.fevertime.backend.domain.LocationType;

@Setter
@Getter
public class ChallengeRequestDto {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int limitPerson;
    private LocationType locationType;
    private String address;
    private String category;
    private ChallengeProgress challengeProgress;
}
