package shop.fevertime.backend.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.fevertime.backend.domain.Challenge;

@Getter
@NoArgsConstructor
public class ChallengeResponseDto {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int limitPerson;
    private boolean onOff;
    private String[] categories;
    private MultipartFile image;

    public ChallengeResponseDto(Challenge challenge){
        BeanUtils.copyProperties(challenge,this);
    }
}
