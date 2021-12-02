package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChallengeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "challenge_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(updatable = false)
    private LocalDateTime missionDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeStatus challengeStatus;

    public ChallengeHistory(
            User user,
            Challenge challenge,
            LocalDateTime createdDate,
            LocalDateTime missionDate,
            ChallengeStatus challengeStatus
    ) {
        this.user = user;
        this.challenge = challenge;
        this.createdDate = createdDate;
        this.missionDate = missionDate;
        this.challengeStatus = challengeStatus;
    }

    public void cancel() {
        this.challengeStatus = ChallengeStatus.CANCEL;
    }

    public void fail() {
        this.challengeStatus = ChallengeStatus.FAIL;
    }
}
