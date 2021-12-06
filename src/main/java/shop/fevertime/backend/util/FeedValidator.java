package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

public class FeedValidator {

    public static void validateFeedCreate(String contents, User user) {
        if (user == null ) {
            throw new ApiRequestException("유저 정보가 유효하지 않습니다.");
        }

        if (contents.trim().length() == 0) {
            throw new ApiRequestException("공백으로 피드를 생성할 수 없습니다.");
        }
    }
}
