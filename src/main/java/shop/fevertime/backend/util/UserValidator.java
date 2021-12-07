package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.exception.ApiRequestException;

public class UserValidator {

    /**
     * 유저 생성 validation
     */
    public static void validateCreateUser(String username, String email, UserRole role, String kakaoId, String imgLink) {
        if (username == null || username.trim().length() == 0) {
            throw new ApiRequestException("입력된 유저 이름이 없습니다.");
        }

        if (username.length() > 8 || username.length() <= 1) {
            throw new ApiRequestException("유저 이름은 1~8자 사이로 입력하세요.");
        }

        if (email == null) {
            throw new ApiRequestException("이메일이 없습니다.");
        }

        if (role == null) {
            throw new ApiRequestException("유저 권한이 없습니다.");
        }

        if (kakaoId == null || kakaoId.trim().length() == 0) {
            throw new ApiRequestException("카카오 아이디 값이 없습니다.");
        }

        if(!URLValidator.urlValidator(imgLink)) {
            throw new ApiRequestException("이미지 링크를 확인해주세요.");
        }
    }

    /**
     * 유저 이름 변경 validation
     */
    public static void validateUpdateName(String username) {
        if (username == null || username.trim().length() == 0) {
            throw new ApiRequestException("입력된 유저 이름이 없습니다.");
        }

        if (username.length() > 8 || username.length() < 1) {
            throw new ApiRequestException("유저 이름은 1~8자 사이로 입력하세요.");
        }
    }

    /**
     * 유저 이미지 변경 validation
     */
    public static void validateUpdateImg(String imgLink) {
        if(!URLValidator.urlValidator(imgLink)) {
            throw new ApiRequestException("이미지 링크를 확인해주세요.");
        }
    }
}
