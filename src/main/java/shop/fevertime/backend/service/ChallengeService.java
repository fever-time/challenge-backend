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

    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, User user) throws IOException {

        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");

        Challenge challenge = new Challenge(requestDto, uploadImageUrl, user);

        String[] categories = requestDto.getCategories();
        for (String htmlClassName : categories) {
            Category category = categoryRepository
                    .findByHtmlClassName(htmlClassName)
                    .orElseThrow(
                            () -> new NoSuchElementException("카테고리 정보 찾기 실패")
                    );
            challenge.addChallengeCategory(category);
        }

        challengeRepository.save(challenge);
    }

    @Transactional
    public void deleteChallenge(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }
}
