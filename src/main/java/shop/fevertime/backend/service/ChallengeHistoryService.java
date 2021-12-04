package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.response.*;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeHistoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;

import java.time.LocalDateTime;
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

    @Transactional
    public ChallengeUserResponseDto getChallengeHistoryUser(Long challengeId, User user) {
        // 챌린지 찾기
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지를 찾을 수 없습니다.")
        );
        // 유저가 챌린지 인증한 리스트 찾기
        List<CertificationResponseDto> certifies = certificationRepository.findAllByChallengeAndUser(challenge, user).stream()
                .map(CertificationResponseDto::new)
                .collect(Collectors.toList());

        List<ChallengeHistoryResponseDto> userHistories = challengeHistoryRepository.findAllByChallengeAndUser(challenge, user).stream()
                .map(ChallengeHistoryResponseDto::new)
                .collect(Collectors.toList());

        return new ChallengeUserResponseDto(user, certifies, userHistories);
    }

    @Transactional
    public List<UserCertifiesResponseDto> getChallengeHistoryUsers(Long challengeId) {
        // 챌린지 찾기
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지를 찾을 수 없습니다.")
        );
        return userRepository.findAllCertifiesByChallenge(challenge).stream()
                .map(user -> new UserCertifiesResponseDto(user, user.getCertificationList()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void joinChallenge(Long challengeId, User user) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지를 찾을 수 없습니다.")
        );
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
                () -> new NoSuchElementException("해당 챌린지를 찾을 수 없습니다.")
        );

        ChallengeHistory challengeHistory = challengeHistoryRepository.findChallengeHistoryByChallengeStatusEquals(
                ChallengeStatus.JOIN,
                user,
                challenge).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지를 참여중인 기록이 없습니다.")
        );

        challengeHistory.cancel();
    }

    public List<UserChallengeResponseDto> getChallengesByUser(User user) {
        return challengeHistoryRepository.findAllByUserAndChallengeStatus(user, ChallengeStatus.JOIN).stream()
                .map(challengeHistory -> new UserChallengeResponseDto(challengeHistory.getChallenge()))
                .collect(Collectors.toList());
    }
}