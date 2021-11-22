package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = true)
    private Long kakaoId;

    public User(String nickname, String encodedPassword, String email, UserRole role, Long kakaoId) {
        this.setUsername(nickname);
        this.setPassword(encodedPassword);
        this.setEmail(email);
        this.setRole(role);
        this.setKakaoId(kakaoId);
    }

    // 생성자

}