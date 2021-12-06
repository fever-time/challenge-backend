package shop.fevertime.backend.util;

import shop.fevertime.backend.dto.request.CertificationRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;

public class CertificationValidator {

    /**
     * 인증 생성 validation
     */
    public static void validateCreate(CertificationRequestDto requestDto, Long userId) {
        if (userId == null || userId < 0) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getContents() == null || requestDto.getContents().trim().length() == 0) {
            throw new ApiRequestException("인증 내용이 없습니다.");
        }

        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }
    }
}
