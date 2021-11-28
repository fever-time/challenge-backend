package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.response.JwtResponse;
import shop.fevertime.backend.dto.request.SocialLoginRequestDto;
import shop.fevertime.backend.service.UserService;
import shop.fevertime.backend.util.JwtTokenUtil;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserApiController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping(value = "/login/kakao")
    public ResponseEntity<?> createAuthenticationTokenByKakao(@RequestBody SocialLoginRequestDto socialLoginDto) throws Exception {
        String kakaoId = userService.kakaoLogin(socialLoginDto.getToken());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(kakaoId);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
    }
}
