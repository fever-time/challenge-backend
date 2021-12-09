package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.dto.request.ChallengeUpdateRequestDto;
import shop.fevertime.backend.util.CertificationValidator;
import shop.fevertime.backend.util.ChallengeValidator;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Enumerated(value = EnumType.STRING)
    private LocationType locationType;

    @Column(nullable = false)
    private String address; // 오프라인으로 선택한 경우 장소 지정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    /**
     * 챌린지 수정 메서드
     */
    public void update(String imgLink, String address) {
        ChallengeValidator.validateUpdate(imgLink, address);
        this.imgLink = imgLink;
        this.address = address;
    }

    /**
     * 챌린지 생성 시 사용하는 생성자
     */
    public Challenge(String title,
                     String description,
                     String imgLink,
                     LocalDateTime startDate,
                     LocalDateTime endDate,
                     int limitPerson,
                     LocationType locationType,
                     String address,
                     User user,
                     Category category
    ) {
        ChallengeValidator.validateCreate(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);
        this.title = title;
        this.description = description;
        this.imgLink = imgLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limitPerson = limitPerson;
        this.locationType = locationType;
        this.address = address;
        this.user = user;
        this.category = category;
    }
}
