package shop.fevertime.backend.domain;

import org.junit.jupiter.api.*;
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

        @Nested
        @DisplayName("정상 케이스")
        class Success {

            @Test
            @DisplayName("댓글 생성")
            void createComment() {
                // given

                // when
                Comment comment = new Comment(feed, contents, user);
                // then
                assertThat(comment.getId()).isNull();
                assertThat(comment.getContents()).isEqualTo(contents);
                assertThat(comment.getFeed()).isEqualTo(feed);
                assertThat(comment.getUser()).isEqualTo(user);
                assertThat(comment.getParent()).isNull();
            }

            @Test
            @DisplayName("대댓글 생성")
            void createChildComment() {
                // given
                Comment parent = new Comment(feed, contents, user);
                // when
                Comment child = Comment.createChildComment(feed, contents, user, parent);
                // then
                assertThat(child.getId()).isNull();
                assertThat(child.getContents()).isEqualTo(contents);
                assertThat(child.getFeed()).isEqualTo(feed);
                assertThat(child.getUser()).isEqualTo(user);
                assertThat(child.getParent()).isEqualTo(parent);
            }
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
                    Exception exception = assertThrows(ApiRequestException.class, () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("공백으로 댓글을 생성할 수 없습니다.");
                }

                @Test
                @DisplayName("null")
                void fail_empty() {
                    // given
                    contents = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.class, () -> new Comment(feed, contents, user));
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
                    Exception exception = assertThrows(ApiRequestException.class, () -> new Comment(feed, contents, user));
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
                    Exception exception = assertThrows(ApiRequestException.class, () -> new Comment(feed, contents, user));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("유저 정보가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("대댓글")
            class child {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    Comment parent = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class, () -> Comment.createChildComment(feed, contents, user, parent));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("댓글 정보가 유효하지 않습니다.");
                }
            }
        }
    }

    @Nested
    @DisplayName("댓글 객체 수정")
    class Update {

        private String contents;
        private String updateContentsString;
        private User user;
        private Feed feed;

        @BeforeEach
        void setup() {
            contents = "댓글1";
            updateContentsString = "수정댓글";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://img.com/img");
            feed = new Feed("피드1", user);
        }

        @Test
        @DisplayName("정상 케이스")
        void update_Normal() {
            // given
            Comment comment = new Comment(feed, contents, user);
            // when
            comment.update(updateContentsString);
            // then
            assertThat(comment.getId()).isNull();
            assertThat(comment.getContents()).isEqualTo(updateContentsString);
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
                    updateContentsString = null;
                    Comment comment = new Comment(feed, contents, user);
                    // when
                    Exception exception = assertThrows(ApiRequestException.class, () -> comment.update(updateContentsString));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("공백으로 댓글을 수정할 수 없습니다.");
                }

                @Test
                @DisplayName("null")
                void fail_empty() {
                    // given
                    updateContentsString = "";
                    Comment comment = new Comment(feed, contents, user);
                    // when
                    Exception exception = assertThrows(ApiRequestException.class, () -> comment.update(updateContentsString));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("공백으로 댓글을 수정할 수 없습니다.");
                }
            }
        }
    }
}