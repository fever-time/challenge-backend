package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

public class CertificationValidator {

    /**
     * 인증 생성 validation
     */
    public static void validateCreate(User user, String imgLink, String contents) {
        if (user == null) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (contents == null || contents.trim().length() == 0) {
            throw new ApiRequestException("인증 내용이 없습니다.");
        }

        if (imgLink == null) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }
    }
}
