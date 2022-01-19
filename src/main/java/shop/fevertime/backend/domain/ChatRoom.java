package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int userCount;

    public ChatRoom(String name, User user, int userCount) {
        this.name = name;
        this.user = user;
        this.userCount = userCount;
    }

    public void addUserCount() {
        this.userCount++;
    }

    public void minusUserCount() {
        this.userCount--;
    }

}
