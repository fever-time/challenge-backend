package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.util.FeedValidator;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Feed(String contents, User user) {
        FeedValidator.validateFeedCreate(contents, user);
        this.contents = contents;
        this.user = user;
    }

    public void update(String contents) {
        FeedValidator.validatorFeedUpdate(contents);
        this.contents = contents;
    }
}
