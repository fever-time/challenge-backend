package shop.fevertime.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.fevertime.backend.util.UserValidator;

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
    private String imgUrl;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Certification> certificationList = new ArrayList<>();

    /**
     * 카카오 로그인 유저 생성자
     */
    public User(String username, String email, UserRole role, String kakaoId, String imgUrl) {
        UserValidator.validateCreateUser(username, email, role, kakaoId, imgUrl);
        this.username = username;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
        this.imgUrl = imgUrl;
    }

    /**
     * 유저 정보 수정 메서드
     */
    public void updateUserImg(String imgUrl) {
        UserValidator.validateUpdateImg(imgUrl);
        this.imgUrl = imgUrl;
    }

    public void updateUsername(String username) {
        UserValidator.validateUpdateName(username);
        this.username = username;
    }
}
