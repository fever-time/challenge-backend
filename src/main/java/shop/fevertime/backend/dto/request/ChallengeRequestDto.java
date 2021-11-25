package shop.fevertime.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ChallengeRequestDto {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int limitPerson;
    private boolean onOff;
    private String[] categories;
    private MultipartFile image;
}
