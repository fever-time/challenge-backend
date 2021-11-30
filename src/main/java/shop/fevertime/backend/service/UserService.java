package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.security.kakao.KakaoOAuth2;
import shop.fevertime.backend.security.kakao.KakaoUserInfo;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;

    public String kakaoLogin(String token) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(token);
        String kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            kakaoUser = new User(nickname, email, UserRole.USER, kakaoId);
            userRepository.save(kakaoUser);
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return kakaoId;
    }
}
