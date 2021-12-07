package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.UserRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;

public class UserValidator {
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
        if (imgLink == null) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }
    }
}
