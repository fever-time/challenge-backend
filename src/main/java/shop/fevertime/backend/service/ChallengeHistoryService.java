package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.ChallengeHistory;
import shop.fevertime.backend.domain.ChallengeStatus;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.repository.ChallengeHistoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChallengeHistoryService {

    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final ChallengeRepository challengeRepository;

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
}
