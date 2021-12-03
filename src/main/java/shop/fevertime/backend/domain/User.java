package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Certification> certificationList = new ArrayList<>();

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

    /**
     * 유저 정보 수정 메서드
     */
    public void update(String username, String imgLink) {
        this.username = username;
        this.imgLink = imgLink;
    }

    public void updateUsername(String username) {
        this.username = username;
    }
}
