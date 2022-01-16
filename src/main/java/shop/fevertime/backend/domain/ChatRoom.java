package shop.fevertime.backend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.dto.ChatRoomDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseTimeEntity {

    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 증가 명령입니다.
    @Column(name = "room_id")
    private Long id;

    @Column // 컬럼 값이고 반드시 값이 존재해야 함을 나타냅니다.
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ChatRoom(ChatRoomDto chatRoomDto, User user) {
        this.name = chatRoomDto.getName();
        this.user = user;
    }

}
