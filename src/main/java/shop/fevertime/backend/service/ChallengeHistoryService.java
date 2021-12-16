package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.response.*;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeHistoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeHistoryService {

    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final CertificationService certificationService;

    @Transactional
    public ChallengeUserResponseDto getChallengeHistoryUser(Long challengeId, User user) {
        // 챌린지 찾기
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("해당 챌린지를 찾을 수 없습니다.")
        );
        // 유저가 챌린지 인증한 리스트 찾기
        List<CertificationResponseDto> certifies = certificationRepository.findAllByChallengeAndUser(challenge, user).stream()
                .map(CertificationResponseDto::new)
                .collect(Collectors.toList());
        // 챌린지 참여 내역
        List<ChallengeHistoryResponseDto> userHistories = challengeHistoryRepository.findAllByChallengeAndUser(challenge, user).stream()
                .map(ChallengeHistoryResponseDto::new)
                .collect(Collectors.toList());

        return new ChallengeUserResponseDto(user, certifies, userHistories);
    }

    @Transactional
    public List<UserCertifiesResponseDto> getChallengeHistoryUsers(Long challengeId) {
        // 챌린지 찾기
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("해당 챌린지를 찾을 수 없습니다.")
        );
        //히스토리에서 해당 챌린지에 조인한 데이터 가져옴 -> 해당 유저 리스트 가져옴
        List<ChallengeHistory> status = challengeHistoryRepository.findAllByChallengeAndChallengeStatus(challenge, ChallengeStatus.JOIN);
        List<User> userList = new ArrayList<>();
        for (ChallengeHistory history : status) {
            userList.add(history.getUser());
        }
        return userList.stream().
                map(user -> new UserCertifiesResponseDto(user, user.getCertificationList()))
                .collect(Collectors.toList());

//        return userRepository.findAllCertifiesByChallenge(challenge).stream()
//                .map(user -> new UserCertifiesResponseDto(user, user.getCertificationList()))
//                .collect(Collectors.toList());
    }

    @Transactional
    public void joinChallenge(Long challengeId, User user) throws ApiRequestException {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("해당 챌린지를 찾을 수 없습니다.")
        );
      
        if (challenge.getChallengeProgress() == ChallengeProgress.STOP) {
            throw new ApiRequestException("종료된 챌린지에 참여할 수 없습니다.");
        }

        //해당 챌린지와 유저로 히스토리 찾아와서 fail 갯수 가져오기
        List<ChallengeHistory> user1 = challengeHistoryRepository.findAllByChallengeAndUserAndChallengeStatus(challenge, user, ChallengeStatus.FAIL);
        if (user1.size() >= 3) throw new ApiRequestException("챌린지에 참여할 수 없습니다.");

        LocalDateTime now = LocalDateTime.now();
        ChallengeHistory challengeHistory = new ChallengeHistory(
                user,
                challenge,
                now,
                now.plusDays(7),
                ChallengeStatus.JOIN);

        challengeHistoryRepository.save(challengeHistory);
    }

    @Transactional
    public void cancelChallenge(Long challengeId, User user) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("해당 챌린지를 찾을 수 없습니다.")
        );

        if (challenge.getChallengeProgress() == ChallengeProgress.STOP) {
            throw new ApiRequestException("종료된 챌린지에 참여 취소할 수 없습니다.");
        }

        ChallengeHistory challengeHistory = challengeHistoryRepository.findChallengeHistoryByChallengeStatusEquals(
                ChallengeStatus.JOIN,
                user,
                challenge).orElseThrow(
                () -> new ApiRequestException("해당 챌린지를 참여중인 기록이 없습니다.")
        );

        certificationRepository.findAllByChallengeAndUser(challenge, user)
                .forEach(certi -> certificationService.deleteCertification(certi.getId(), user));

        challengeHistory.cancel();
    }

    public List<UserChallengeResponseDto> getChallengesByUser(User user) {
        return challengeHistoryRepository.findAllByUserAndChallengeStatus(user, ChallengeStatus.JOIN).stream()
                .map(challengeHistory -> new UserChallengeResponseDto(challengeHistory.getChallenge()))
                .collect(Collectors.toList());
    }
}
