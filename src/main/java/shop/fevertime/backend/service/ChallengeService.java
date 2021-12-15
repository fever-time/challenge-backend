package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import shop.fevertime.backend.util.LocalDateTimeUtil;
import shop.fevertime.backend.util.S3Uploader;

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
    private final ChallengeHistoryService challengeHistoryService;

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


    public List<ChallengeResponseDto> getChallengesByFilter(String sortBy) {
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        List<Challenge> getChallenges;
        if (Objects.equals(sortBy, "inProgress")) {
            getChallenges = challengeRepository.findAllByChallengeProgress(ChallengeProgress.INPROGRESS);
        } else {
            getChallenges = challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        }
        getChallengesWithParticipants(challengeResponseDtoList, getChallenges);
        return challengeResponseDtoList;
    }


    public ChallengeResponseDto getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("해당 아이디가 존재하지 않습니다.")
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
    public void createChallenge(ChallengeRequestDto requestDto, User user, MultipartFile image) throws IOException {
        String uploadImageUrl;
        // 이미지 첨부 안 했을 때 기본이미지
        if (image == null) {
            uploadImageUrl = "https://fever-prac.s3.ap-northeast-2.amazonaws.com/challenge/challenge.jfif";
        } else {
            // 이미지 AWS S3 업로드
            uploadImageUrl = s3Uploader.upload(image, "challenge");
        }

        // 카테고리 찾기
        Category category = categoryRepository.findByName(requestDto.getCategory()).orElseThrow(
                () -> new ApiRequestException("카테고리 정보 찾기 실패")
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
                category,
                requestDto.getChallengeProgress()
        );
        challengeRepository.save(challenge);
        //챌린지 생성한 유저는 자동으로 챌린지 참가 상태로 저장
        challengeHistoryService.joinChallenge(challenge.getId(), user);

    }

    @Transactional
    public void updateChallenge(Long challengeId, ChallengeUpdateRequestDto requestDto, User user, MultipartFile image) throws IOException {
        // 챌린지 이미지 s3에서 기존 이미지 삭제
        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("해당 챌린지가 존재하지 않습니다.")
        );

        //이미지가 null일때 장소만 업데이트
        if (image == null) {
            challenge.updateAddress(requestDto.getAddress());
            return;
        }

        // 기존 이미지 S3에서 삭제 (기본 이미지 아닐 경우만 )
        if (!Objects.equals(challenge.getImgUrl(), "https://fever-prac.s3.ap-northeast-2.amazonaws.com/challenge/challenge.jfif")) {
            String[] ar = challenge.getImgUrl().split("/");
            s3Uploader.delete(ar[ar.length - 1], "challenge");
        }

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(image, "challenge");

        // 해당 챌린지의 필드 값을 변경 -> 변경 감지
        challenge.update(uploadImageUrl, requestDto.getAddress());
    }

    @Transactional
    public void deleteChallenge(Long challengeId, User user) {

        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("해당 챌린지가 존재하지 않습니다.")
        );

        //챌린지에 참가한 유저 검토
        List<ChallengeHistory> all = challengeHistoryRepository.findAllByChallengeAndChallengeStatusAndUserNot(challenge, ChallengeStatus.JOIN, user);

        //생성 유저 제외하여 참가자가 없으면 삭제
        if (all.size() == 0) {
            //히스토리 삭제
            challengeHistoryRepository.deleteAllByChallenge(challenge);

            // 챌린지 이미지 s3에서 삭제
            String[] ar = challenge.getImgUrl().split("/");
            s3Uploader.delete(ar[ar.length - 1], "challenge");

            // 삭제하는 챌린지에 해당하는 인증 이미지 s3 삭제
            List<Certification> certifications = certificationRepository.findAllByChallenge(challenge);
            for (Certification certification : certifications) {
                String[] arr = certification.getImgUrl().split("/");
                s3Uploader.delete(arr[arr.length - 1], "certification");
            }
            certificationRepository.deleteAllByChallenge(challenge);
            challengeRepository.delete(challenge);
        } else {
            throw new ApiRequestException("챌린지를 삭제할 수 없습니다.");
        }

    }

    public ResultResponseDto checkChallengeCreator(Long challengeId, User user) {
        boolean present = challengeRepository.findByIdAndUser(challengeId, user).isPresent();
        if (present) {
            return new ResultResponseDto("success", "챌린지 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "챌린지 생성자가 아닙니다.");
    }
}
