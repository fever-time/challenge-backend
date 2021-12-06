package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.request.ChallengeUpdateRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeHistoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.util.ChallengeValidator;
import shop.fevertime.backend.util.LocalDateTimeUtil;
import shop.fevertime.backend.util.S3Uploader;

import javax.security.sasl.AuthenticationException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final CertificationRepository certificationRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;
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
        long participants = challengeHistoryRepository.countDistinctUserByChallengeAndChallengeStatus(challenge, ChallengeStatus.JOIN);
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
            long participants = challengeHistoryRepository.countDistinctUserByChallengeAndChallengeStatus(getChallenge, ChallengeStatus.JOIN);
            ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto(getChallenge, participants);
            challengeResponseDtoList.add(challengeResponseDto);
        }
    }

    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, User user) throws IOException {
        //validation
        ChallengeValidator.validateCreate(requestDto,user.getId());
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
                requestDto.getLocationType(),
                requestDto.getAddress(),
                user,
                category
        );
        challengeRepository.save(challenge);
    }

    @Transactional
    public void updateChallenge(Long challengeId, ChallengeUpdateRequestDto requestDto, User user) throws IOException {
        // 챌린지 이미지 s3에서 기존 이미지 삭제
        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("해당 챌린지가 존재하지 않습니다.")
        );

        // 기존 이미지 S3에서 삭제
        String[] ar = challenge.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");

        // 해당 챌린지의 필드 값을 변경 -> 변경 감지
        challenge.update(uploadImageUrl, requestDto.getAddress());
    }

    @Transactional
    public void deleteChallenge(Long challengeId, User user) {
        // 챌린지 이미지 s3에서 삭제
        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("해당 챌린지가 존재하지 않습니다.")
        );
        String[] ar = challenge.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        // 삭제하는 챌린지에 해당하는 인증 이미지 s3 삭제
        List<Certification> certifications = certificationRepository.findAllByChallenge(challenge);
        for (Certification certification : certifications) {
            String[] arr = certification.getImgLink().split("/");
            s3Uploader.delete(arr[arr.length - 1], "certification");
        }

        certificationRepository.deleteAllByChallenge(challenge);
        challengeRepository.delete(challenge);
    }

    public ResultResponseDto checkChallengeCreator(Long challengeId, User user) {
        boolean present = challengeRepository.findByIdAndUser(challengeId, user).isPresent();
        if (present) {
            return new ResultResponseDto("success", "챌린지 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "챌린지 생성자가 아닙니다.");
    }
}
