package shop.fevertime.backend.util;

import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.exception.ApiRequestException;

public class CommentValidator {

    public static void validateCommentCreate(String contents, User user, Feed feed) {
        if (user == null) {
            throw new ApiRequestException("유저 정보가 유효하지 않습니다.");
        }

        if (feed == null) {
            throw new ApiRequestException("피드 정보가 유효하지 않습니다.");
        }

        if (contents == null || contents.trim().length() == 0) {
            throw new ApiRequestException("공백으로 댓글을 생성할 수 없습니다.");
        }
    }

    public static void validateChildCommentCreate(String contents, User user, Feed feed, Comment parent) {
        if (user == null) {
            throw new ApiRequestException("유저 정보가 유효하지 않습니다.");
        }

        if (feed == null) {
            throw new ApiRequestException("피드 정보가 유효하지 않습니다.");
        }

        if (contents == null || contents.trim().length() == 0) {
            throw new ApiRequestException("공백으로 댓글을 생성할 수 없습니다.");
        }

        if(parent == null) {
            throw  new ApiRequestException("댓글 정보가 유효하지 않습니다.");
        }
    }

    public static void validatorCommentUpdate(String contents) {
        if (contents == null || contents.trim().length() == 0) {
            throw new ApiRequestException("공백으로 댓글을 수정할 수 없습니다.");
        }
    }
}
