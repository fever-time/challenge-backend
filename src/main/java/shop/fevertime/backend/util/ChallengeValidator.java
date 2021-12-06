package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.LocationType;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.exception.ApiRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class ChallengeValidator {

    /**
     * 챌린지 생성 validation
     */
    public static void validateCreate(ChallengeRequestDto requestDto, Long userId) {
        if (userId == null || userId < 0) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().trim().length() == 0) {
            throw new ApiRequestException("챌린지 이름이 없습니다.");
        }

        if (requestDto.getDescription() == null || requestDto.getDescription().trim().length() == 0) {
            throw new ApiRequestException("챌린지 상세 설명이 없습니다.");
        }

        if (requestDto.getLimitPerson() > 33 || requestDto.getLimitPerson() < 1) {
            throw new ApiRequestException("제한 인원은 1명~33명 사이로 입력하세요");
        }

        if (requestDto.getLocationType() == LocationType.OFFLINE && Objects.equals(requestDto.getAddress(), "")) {
            throw new ApiRequestException("오프라인인 경우 주소를 입력하세요.");
        }

        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }

        if (!validationDate(requestDto.getStartDate()) || !validationDate(requestDto.getEndDate())) {
            throw new ApiRequestException("날짜 형식이 알맞지 않습니다.");
        }
    }

    public static boolean validationDate(String checkDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);
            dateFormat.parse(checkDate);
            return false;

        } catch (ParseException e) {
            return true;
        }
    }
}
