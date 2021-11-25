package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imgLink;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int limitPerson;

    @Column(nullable = false)
    private boolean onOff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeCategory> challengeCategories = new ArrayList<>();

    /**
     * 챌린지 생성 시 사용하는 생성자
     */
    public Challenge(ChallengeRequestDto requestDto, String uploadImageUrl, User user) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.imgLink = uploadImageUrl;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.startDate = LocalDate.parse(requestDto.getStartDate(), dateTimeFormatter).atStartOfDay();
        this.endDate = LocalDate.parse(requestDto.getEndDate(), dateTimeFormatter).atStartOfDay();

        this.limitPerson = requestDto.getLimitPerson();
        this.onOff = requestDto.isOnOff();

        this.user = user;
    }

    public void addChallengeCategory(Category category) {
        ChallengeCategory challengeCategory = new ChallengeCategory(category, this);
        challengeCategories.add(challengeCategory);
    }

}
