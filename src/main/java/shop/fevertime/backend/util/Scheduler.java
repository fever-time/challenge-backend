package shop.fevertime.backend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.ChallengeProgress;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.ChallengeRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private final ChallengeRepository challengeRepository;

    @Scheduled(cron = "0 0 24 * * *")
    @Transactional
    public void updateChallengeProcess() throws ApiRequestException {
        for (Challenge challenge : challengeRepository.findAllByChallengeProgress(ChallengeProgress.INPROGRESS)) {
            if (LocalDate.now().isAfter(challenge.getEndDate().toLocalDate())) {
                challenge.updateChallengeProcess(ChallengeProgress.STOP);
            }
            throw new ApiRequestException("asdf");
        }
    }
}
