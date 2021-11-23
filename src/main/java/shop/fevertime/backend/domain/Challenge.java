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
    private String img;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int people;

    @Column(nullable = false)
    private boolean onOff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeCategory> challengeCategories = new ArrayList<>();

    public Challenge(String title, String description, String img, LocalDateTime startDate, LocalDateTime endDate, int people, boolean onOff, User user, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.img = img;
        this.startDate = startDate;
        this.endDate = endDate;
        this.people = people;
        this.onOff = onOff;
        this.user = user;

        for (Category category : categories) {
            ChallengeCategory challengeCategory = new ChallengeCategory(category, this);
            challengeCategories.add(challengeCategory);
        }
    }
}
