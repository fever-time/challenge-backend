package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.ChallengeStatus;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

import java.time.LocalDateTime;

public class ChallengeHistoryValidator {

    public static void validateCreate(User user, Challenge challenge, LocalDateTime createdDate, LocalDateTime missionDate, ChallengeStatus challengeStatus) {
        if (user == null) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (challenge == null) {
            throw new ApiRequestException("챌린지가 유효하지 않습니다.");
        }

        if (createdDate == null) {
            throw new ApiRequestException("챌린지 참가 날짜가 없습니다.");
        }
        if (missionDate == null) {
            throw new ApiRequestException("챌린지 미션 날짜가 없습니다.");
        }

        if (challengeStatus == null) {
            throw new ApiRequestException("챌린지 참여 상태가 없습니다.");
        }
    }
}
