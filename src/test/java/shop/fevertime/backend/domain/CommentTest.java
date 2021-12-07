package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.fevertime.backend.exception.ApiRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Nested
    @DisplayName("댓글 객체 생성")
    class CreateComment {

        private String contents;
        private User user;
        private Feed feed;

        @BeforeEach
        void setup() {
            contents = "댓글1";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://img.com/img");
            feed = new Feed("피드1", user);
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            Comment comment = new Comment(feed, contents, user);
            // then
            assertThat(comment.getId()).isNull();
            assertThat(comment.getContents()).isEqualTo(contents);
            assertThat(comment.getFeed()).isEqualTo(feed);
            assertThat(comment.getUser()).isEqualTo(user);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("댓글 내용")
            class Contents {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    contents = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("공백으로 댓글을 생성할 수 없습니다.");
                }

                @Test
                @DisplayName("null")
                void fail_empty() {
                    // given
                    contents = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("공백으로 댓글을 생성할 수 없습니다.");
                }
            }

            @Nested
            @DisplayName("댓글 피드")
            class Feed {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    feed = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("피드 정보가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("댓글 유저")
            class User {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    user = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("유저 정보가 유효하지 않습니다.");
                }
            }
        }
    }
}