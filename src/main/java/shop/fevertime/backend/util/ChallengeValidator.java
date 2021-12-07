package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.LocationType;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public class ChallengeValidator {

    /**
     * 챌린지 생성 validation
     */
    public static void validateCreate(String title,
                                      String description,
                                      String imgLink,
                                      LocalDateTime startDate,
                                      LocalDateTime endDate,
                                      int limitPerson,
                                      LocationType locationType,
                                      String address,
                                      User user) {
        if (user == null) {
            throw new ApiRequestException("유저 Id 가 유효하지 않습니다.");
        }

        if (title == null || title.trim().length() == 0) {
            throw new ApiRequestException("챌린지 이름이 없습니다.");
        }

        if (description == null || description.trim().length() == 0) {
            throw new ApiRequestException("챌린지 상세 설명이 없습니다.");
        }

        if (limitPerson > 33 || limitPerson < 1) {
            throw new ApiRequestException("제한 인원은 1명~33명 사이로 입력하세요");
        }

        if (locationType == LocationType.OFFLINE && Objects.equals(address, "")) {
            throw new ApiRequestException("오프라인인 경우 주소를 입력하세요.");
        }

        if (imgLink == null) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }

        if (!validationDate(startDate) || !validationDate(endDate)) {
            throw new ApiRequestException("날짜 형식이 알맞지 않습니다.");
        }
    }

    /**
     * 챌린지 변경 validation
     */
    public static void validateUpdate(String imgLink, String address) {
        //address 상의 후

        if (imgLink == null) {
            throw new ApiRequestException("첨부된 파일이 없습니다.");
        }
    }


    public static boolean validationDate(LocalDateTime checkDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);
            dateFormat.parse(String.valueOf(checkDate));
            return true;

        } catch (ParseException e) {
            return false;
        }
    }
}
