package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
            user = new User("test", "test@email.com", UserRole.USER, "123456", "");
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
                    Comment comment = new Comment(feed, contents, user);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    contents = "";

                    // when
                    Comment comment = new Comment(feed, contents, user);

                    // then
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
                    Comment comment = new Comment(feed, contents, user);

                    // then
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
                    Comment comment = new Comment(feed, contents, user);

                    // then
                }
            }
        }
    }
}