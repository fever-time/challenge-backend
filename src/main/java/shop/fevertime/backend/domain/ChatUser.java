package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chatUser_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public ChatUser(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
