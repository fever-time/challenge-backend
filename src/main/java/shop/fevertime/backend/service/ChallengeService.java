package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Category;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final CertificationRepository certificationRepository;
    private final S3Uploader s3Uploader;

    //카테고리에 해당하는 챌린지 찾아오기
    public List<ChallengeResponseDto> getChallenges(String category) {
        if (Objects.equals(category, "All")) {
            return challengeRepository.findAll().stream()
                    .map(ChallengeResponseDto::new)
                    .collect(Collectors.toList());
        }
        return challengeRepository.findAllByCategoryNameEquals(category)
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

    //검색어에 해당하는 챌린지 찾아오기
    public List<ChallengeResponseDto> searchChallenges(String search) {
        return challengeRepository.findAllByTitleContaining(search)
                .stream()
                .map(ChallengeResponseDto::new)
                .collect(Collectors.toList());
    }

    //새로운 챌린지 생성
    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, User user) throws IOException {

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");
        // 카테고리 찾기
        Category category = categoryRepository.findByName(requestDto.getCategory()).orElseThrow(
                () -> new NoSuchElementException("카테고리 정보 찾기 실패")
        );

        // 챌린지 생성
        Challenge challenge = new Challenge(
                requestDto.getTitle(),
                requestDto.getDescription(),
                uploadImageUrl,
                LocalDateTimeUtil.getLocalDateTime(requestDto.getStartDate()),
                LocalDateTimeUtil.getLocalDateTime(requestDto.getEndDate()),
                requestDto.getLimitPerson(),
                requestDto.isOnOff(),
                user,
                category
        );

        challengeRepository.save(challenge);
    }

    //챌린지 삭제
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
