package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String imgLink;

    /**
     * 카카오 로그인 유저 생성자
     */
    public User(String username, String email, UserRole role, String kakaoId, String imgLink) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
        this.imgLink = imgLink;
    }
}
