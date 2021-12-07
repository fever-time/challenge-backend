package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

public class CertificationValidator {

    /**
     * 인증 생성 validation
     */
    public static void validateCreate(User user, String imgLink, String contents, Challenge challenge) {
        if (user == null) {
            throw new ApiRequestException("유저 정보가 유효하지 않습니다.");
        }

        if (contents == null || contents.trim().length() == 0) {
            throw new ApiRequestException("인증 내용이 없습니다.");
        }

        if (imgLink == null || imgLink.trim().length() == 0) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }

        if (!URLValidator.urlValidator(imgLink)) {
            throw new ApiRequestException("이미지 링크를 확인해주세요.");
        }

        if (challenge == null) {
            throw new ApiRequestException("챌린지 정보가 유효하지 않습니다.");
        }
    }
}
