package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Category;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.util.LocalDateTimeUtil;
import shop.fevertime.backend.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;

    public List<ChallengeResponseDto> getChallenges() {
        return challengeRepository.findAll()
                .stream()
                .map(ChallengeResponseDto::new)
                .collect(Collectors.toList());
    }

    public ChallengeResponseDto getChallenge(Long id) {
        return challengeRepository.findById(id)
                .map(ChallengeResponseDto::new)
                .orElseThrow(
                        () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
                );
    }

    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, User user) throws IOException {

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");
        // 카테고리 찾기
        Category category = categoryRepository.findByHtmlClassName(requestDto.getCategory()).orElseThrow(
                () -> new NoSuchElementException("카테고리 정보 찾기 실패")
        );

        // 챌린지 생성
        Challenge challenge = new Challenge(
                requestDto.getTitle(),
                requestDto.getDescription(),
                "uploadImageUrl",
                LocalDateTimeUtil.getLocalDateTime(requestDto.getStartDate()),
                LocalDateTimeUtil.getLocalDateTime(requestDto.getEndDate()),
                requestDto.getLimitPerson(),
                requestDto.isOnOff(),
                user,
                category
        );

        challengeRepository.save(challenge);
    }

    @Transactional
    public void deleteChallenge(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }
}
