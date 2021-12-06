package shop.fevertime.backend.util;

import shop.fevertime.backend.dto.request.UserRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;

public class UserValidator {
    /**
     * 유저 이름 변경 validation
     */
    public static void validateUpdateName(UserRequestDto requestDto, Long userId) {
        if (userId == null || userId < 0) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getUsername() == null || requestDto.getUsername().trim().length() == 0) {
            throw new ApiRequestException("입력된 유저 이름이 없습니다.");
        }

        if (requestDto.getUsername().length() > 8 || requestDto.getUsername().length() < 1) {
            throw new ApiRequestException("유저 이름은 1~8자 사이로 입력하세요.");
        }
    }

    /**
     * 유저 이미지 변경 validation
     */
    public static void validateUpdateImg(UserRequestDto requestDto, Long userId) {
        if (userId == null || userId < 0) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }
    }
}
