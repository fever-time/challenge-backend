package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "challenge")
    private List<Certification> certifications;

    /**
     * 챌린지 생성 시 사용하는 생성자
     */
    public Challenge(String title,
                     String description,
                     String imgLink,
                     LocalDateTime startDate,
                     LocalDateTime endDate,
                     int limitPerson,
                     boolean onOff,
                     User user,
                     Category category
    ) {
        this.title = title;
        this.description = description;
        this.imgLink = imgLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limitPerson = limitPerson;
        this.onOff = onOff;
        this.user = user;
        this.category = category;
    }
}
