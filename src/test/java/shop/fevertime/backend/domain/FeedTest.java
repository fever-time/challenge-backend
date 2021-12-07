package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FeedTest {

    @Nested
    @DisplayName("피드 객체 생성")
    class CreateComment {

        private String contents;
        private User user;

        @BeforeEach
        void setup() {
            contents = "피드1";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "");
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            Feed feed = new Feed(contents, user);

            // then
            assertThat(feed.getId()).isNull();
            assertThat(feed.getContents()).isEqualTo(contents);
            assertThat(feed.getUser()).isEqualTo(user);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("피드 내용")
            class Contents {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    contents = null;

                    // when
                    Feed feed = new Feed(contents, user);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    contents = "";

                    // when
                    Feed feed = new Feed(contents, user);

                    // then
                }
            }

            @Nested
            @DisplayName("피드 유저")
            class User {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    user = null;

                    // when
                    Feed feed = new Feed(contents, user);

                    // then
                }
            }
        }
    }
}