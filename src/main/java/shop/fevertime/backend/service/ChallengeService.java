package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Category;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.util.LocalDateTimeUtil;
import shop.fevertime.backend.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final CertificationRepository certificationRepository;
    private final S3Uploader s3Uploader;

    public List<ChallengeResponseDto> getChallenges(String category) {
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        List<Challenge> getChallenges;

        if (Objects.equals(category, "All")) {
            getChallenges = challengeRepository.findAll();
        } else {
            getChallenges = challengeRepository.findAllByCategoryNameEquals(category);
        }
        getChallengesWithParticipants(challengeResponseDtoList, getChallenges);
        return challengeResponseDtoList;
    }

    public ChallengeResponseDto getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        // 챌린지 참여자 수
        long participants = certificationRepository.countDistinctUserIdByChallenge(challenge);
        return new ChallengeResponseDto(challenge, participants);
    }

    public List<ChallengeResponseDto> searchChallenges(String search) {
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        List<Challenge> getChallenges = challengeRepository.findAllByTitleContaining(search);
        getChallengesWithParticipants(challengeResponseDtoList, getChallenges);
        return challengeResponseDtoList;
    }

    /**
     * ChallengeService method
     *
     * @param challengeResponseDtoList - 결과 추가할 리스트
     * @param getChallenges            - challengeRepository 으로 가져온 Challege 리스트
     */
    private void getChallengesWithParticipants(List<ChallengeResponseDto> challengeResponseDtoList, List<Challenge> getChallenges) {
        for (Challenge getChallenge : getChallenges) {
            long participants = certificationRepository.countDistinctUserIdByChallenge(getChallenge);
            ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto(getChallenge, participants);
            challengeResponseDtoList.add(challengeResponseDto);
        }
    }

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

    @Transactional
    public void deleteChallenge(Long challengeId) {
        // 챌린지 이미지 s3에서 삭제
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        String[] ar = challenge.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        // 삭제하는 챌린지에 해당하는 인증 이미지 s3 삭제
        List<Certification> certifications = certificationRepository.findAllByChallengeId(challengeId);
        for (Certification certification : certifications) {
            String[] arr = certification.getImg().split("/");
            s3Uploader.delete(arr[arr.length - 1], "certification");
        }

        certificationRepository.deleteAllByChallengeId(challengeId);
        challengeRepository.deleteById(challengeId);
    }
}
