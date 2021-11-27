package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.dto.request.CommentRequestDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    // 댓글 생성자
    public Comment(Feed feed, CommentRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.feed = feed;
        this.user = user;
    }

    // 댓글 수정
    public void commentUpdate(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
