package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

public class CommentValidator {

    public static void validateCommentCreate(String contents, User user, Feed feed) {
        if (user == null ) {
            throw new ApiRequestException("유저 정보가 유효하지 않습니다.");
        }

        if (feed == null) {
            throw new ApiRequestException("피드 정보가 유효하지 않습니다.");
        }

        if (contents.trim().length() == 0) {
            throw new ApiRequestException("공백으로 댓글을 생성할 수 없습니다.");
        }
    }

}
