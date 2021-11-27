package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.response.CertificationResponseDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.util.LocalDateTimeUtil;
import shop.fevertime.backend.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final CertificationRepository certificationRepository;
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

        // 챌린지 생성
        Challenge challenge = new Challenge(
                requestDto.getTitle(),
                requestDto.getDescription(),
                uploadImageUrl,
                LocalDateTimeUtil.getLocalDateTime(requestDto.getStartDate()),
                LocalDateTimeUtil.getLocalDateTime(requestDto.getEndDate()),
                requestDto.getLimitPerson(),
                requestDto.isOnOff(),
                user
        );

        // 챌린지 카테고리 넣기
        Arrays.stream(requestDto.getCategories())
                .map(htmlClassName -> categoryRepository
                        .findByHtmlClassName(htmlClassName)
                        .orElseThrow(
                                () -> new NoSuchElementException("카테고리 정보 찾기 실패")
                        )).forEach(challenge::addChallengeCategory);

        challengeRepository.save(challenge);
    }

    @Transactional
    public void deleteChallenge(Long challengeId) {
        //이미지 s3에서 삭제
        ChallengeResponseDto responseDto = challengeRepository.findById(challengeId)
                .map(ChallengeResponseDto::new)
                .orElseThrow(
                        () -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        String[] ar = responseDto.getImage().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        //삭제하는 챌린지에 해당하는 인증 이미지 s3 삭제
        List<CertificationResponseDto> certifications = responseDto.getCertifications();
        for (CertificationResponseDto certification : certifications) {
            String[] arr = certification.getImg().split("/");
            s3Uploader.delete(arr[arr.length - 1], "certification");
        }

        certificationRepository.deleteByChallengeId(challengeId);
        challengeRepository.deleteById(challengeId);
    }
}
