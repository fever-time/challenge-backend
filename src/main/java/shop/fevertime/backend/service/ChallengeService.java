package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
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


    public List<ChallengeResponseDto> getChallengesByFilter(String sortBy) {
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        List<Challenge> getChallenges;
        if (Objects.equals(sortBy, "inProgress")) {
            getChallenges = challengeRepository.findAllByChallengeProgress(ChallengeProgress.INPROGRESS);
        } else if (Objects.equals(sortBy, "createdAt")) {
            getChallenges = challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        } else {
            throw new ApiRequestException("????????? ?????? ???????????????.");
        }

        getChallengesWithParticipants(challengeResponseDtoList, getChallenges);
        return challengeResponseDtoList;
    }

    public ChallengeResponseDto getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("?????? ???????????? ???????????? ????????????.")
        );
        // ????????? ????????? ???
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
     * @param challengeResponseDtoList - ?????? ????????? ?????????
     * @param getChallenges            - challengeRepository ?????? ????????? Challege ?????????
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
        // ????????? AWS S3 ?????????
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");
        // ???????????? ??????
        Category category = categoryRepository.findByName(requestDto.getCategory()).orElseThrow(
                () -> new ApiRequestException("???????????? ?????? ?????? ??????")
        );
        // ????????? ??????
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
                ChallengeProgress.INPROGRESS
        );
        challengeRepository.save(challenge);
    }

    @Transactional
    public void updateChallenge(Long challengeId, ChallengeUpdateRequestDto requestDto, User user) throws IOException {
        // ????????? ????????? s3?????? ?????? ????????? ??????
        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("?????? ???????????? ???????????? ????????????.")
        );

        // ?????? ????????? S3?????? ??????
        String[] ar = challenge.getImgUrl().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        // ????????? AWS S3 ?????????
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "challenge");

        // ?????? ???????????? ?????? ?????? ?????? -> ?????? ??????
        challenge.update(uploadImageUrl, requestDto.getAddress());
    }

    @Transactional
    public void deleteChallenge(Long challengeId, User user) {
        // ????????? ????????? s3?????? ??????
        Challenge challenge = challengeRepository.findByIdAndUser(challengeId, user).orElseThrow(
                () -> new ApiRequestException("?????? ???????????? ???????????? ????????????.")
        );
        String[] ar = challenge.getImgUrl().split("/");
        s3Uploader.delete(ar[ar.length - 1], "challenge");

        // ???????????? ???????????? ???????????? ?????? ????????? s3 ??????
        List<Certification> certifications = certificationRepository.findAllByChallenge(challenge);
        for (Certification certification : certifications) {
            String[] arr = certification.getImgUrl().split("/");
            s3Uploader.delete(arr[arr.length - 1], "certification");
        }

        certificationRepository.deleteAllByChallenge(challenge);
        challengeRepository.delete(challenge);
    }

    public ResultResponseDto checkChallengeCreator(Long challengeId, User user) {
        boolean present = challengeRepository.findByIdAndUser(challengeId, user).isPresent();
        if (present) {
            return new ResultResponseDto("success", "????????? ???????????? ????????????.");
        }
        return new ResultResponseDto("fail", "????????? ???????????? ????????????.");
    }
}
