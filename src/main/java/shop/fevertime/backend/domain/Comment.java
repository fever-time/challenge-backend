package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.util.CommentValidator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    // 연관관계 메서드
    public void addChildComment(Comment child) {
        this.child.add(child);
        child.setParent(this);
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }

    // 댓글 생성자
    public Comment(Feed feed, String contents, User user) {
        CommentValidator.validateCommentCreate(contents, user, feed);
        this.contents = contents;
        this.feed = feed;
        this.user = user;
    }

    // 대댓글 생성자
    public Comment(Feed feed, String contents, User user, Comment parent) {
        CommentValidator.validateChildCommentCreate(contents, user, feed, parent);
        this.contents = contents;
        this.feed = feed;
        this.user = user;
        this.parent = parent;
    }

    // 댓글 수정
    public void update(String contents) {
        CommentValidator.validatorCommentUpdate(contents);
        this.contents = contents;
    }
}
