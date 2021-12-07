package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.dto.request.UserRequestDto;

import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.UserChallengeResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.ChallengeHistoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.security.kakao.KakaoOAuth2;
import shop.fevertime.backend.security.kakao.KakaoUserInfo;
import shop.fevertime.backend.util.CertificationValidator;
import shop.fevertime.backend.util.ChallengeValidator;
import shop.fevertime.backend.util.S3Uploader;
import shop.fevertime.backend.util.UserValidator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final S3Uploader s3Uploader;
    private final ChallengeRepository challengeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;
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

        return kakaoId;
    }

    @Transactional
    public List<UserChallengeResponseDto> getChallenges(User user) {
        return challengeRepository.findAllByUser(user).stream()
                .map(UserChallengeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FeedResponseDto> getFeeds(Long id) {
        return feedRepository.findAllByUserId(id)
                .stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserImg(User user, UserRequestDto requestDto) throws IOException {
        User findUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ApiRequestException("해당 아이디가 존재하지 않습니다.")
        );
        // 기존 이미지 S3에서 삭제
        String[] ar = findUser.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "user");

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "user");
        findUser.updateUserimg(uploadImageUrl);
    }

    @Transactional
    public void updateUsername(User user, UserRequestDto requestDto) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ApiRequestException("해당 아이디가 존재하지 않습니다.")
        );

        findUser.updateUsername(requestDto.getUsername());
    }
}
