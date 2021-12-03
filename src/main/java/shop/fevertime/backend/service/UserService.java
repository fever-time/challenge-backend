package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.dto.request.UserRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.security.kakao.KakaoOAuth2;
import shop.fevertime.backend.security.kakao.KakaoUserInfo;
import shop.fevertime.backend.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final S3Uploader s3Uploader;
    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
    private final FeedRepository feedRepository;

    public String kakaoLogin(String token) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(token);
        String kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();
        String defaultImgLink = "변경 필요";

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            kakaoUser = new User(nickname, email, UserRole.USER, kakaoId, defaultImgLink);
            userRepository.save(kakaoUser);
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return kakaoId;
    }

    @Transactional
    public List<ChallengeResponseDto> getChallenges(String kakaoId) {
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        List<Challenge> getChallenges = challengeRepository.findAllByUserKakaoId(kakaoId);
        for (Challenge getChallenge : getChallenges) {
            long participants = certificationRepository.countDistinctUserIdByChallenge(getChallenge);
            ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto(getChallenge, participants);
            challengeResponseDtoList.add(challengeResponseDto);
        }
        return challengeResponseDtoList;
    }

    @Transactional
    public List<FeedResponseDto> getFeeds(Long id) {
        return feedRepository.findAllByUserId(id)
                .stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(Long userId, UserRequestDto requestDto) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        // 기존 이미지 S3에서 삭제
        String[] ar = user.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "user");

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "user");

        user.update(requestDto.getUsername(), uploadImageUrl);

    }

    @Transactional
    public void updateUsername(Long userId, UserRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

        user.updateUsername(requestDto.getUsername());
    }
}
