package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Nested
    @DisplayName("유저 객체 생성")
    class CreateUser {

        private String username;
        private String email;
        private UserRole role;
        private String kakaoId;
        private String imgLink;

        @BeforeEach
        void setup() {
            username = "test";
            email = "tes@email.com";
            role = UserRole.USER;
            kakaoId = "12456";
            imgLink = "";
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            User user = new User(username, email, role, kakaoId, imgLink);

            // then
            assertThat(user.getId()).isNull();
            assertThat(user.getUsername()).isEqualTo(username);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getRole()).isEqualTo(role);
            assertThat(user.getKakaoId()).isEqualTo(kakaoId);
            assertThat(user.getImgLink()).isEqualTo(imgLink);

        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("유저 이름")
            class Name {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    username = null;

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    username = "";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
            }

            @Nested
            @DisplayName("유저 이메일")
            class Email {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    email = null;

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    email = "";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
                @Test
                @DisplayName("이메일 형식")
                void fail_email_form() {
                    // given
                    email = "test_email";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
            }

            @Nested
            @DisplayName("유저 권한")
            class Role {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    role = null;

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
            }

            @Nested
            @DisplayName("유저 카카오아이디")
            class KakaoId {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    kakaoId = null;

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    kakaoId = "";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
            }

            @Nested
            @DisplayName("유저 이미지 링크")
            class ImgLink {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    imgLink = null;

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    imgLink = "";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }

                @Test
                @DisplayName("URL 형식")
                void fail_url_form() {
                    // given
                    imgLink = "test_email";

                    // when
                    User user = new User(username, email, role, kakaoId, imgLink);

                    // then
                }
            }
        }
    }
}