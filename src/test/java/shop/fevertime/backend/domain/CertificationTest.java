package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.fevertime.backend.exception.ApiRequestException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CertificationTest {

    @Nested
    @DisplayName("챌린지 참여기록 객체 생성")
    class CreateCertification {

        private String imgLink;
        private String contents;
        private User user;
        private Challenge challenge;

        @BeforeEach
        void setup() {
            imgLink = "https://img.com/img";
            contents = "챌린지 인증";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://img.com/img");
            Category category = new Category("운동");
            challenge = new Challenge("제목", "설명", "https://img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.ONLINE, "", user, category);
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            Certification certification = new Certification(imgLink, contents, user, challenge);

            // then
            assertThat(certification.getImgLink()).isEqualTo(imgLink);
            assertThat(certification.getContents()).isEqualTo(contents);
            assertThat(certification.getId()).isNull();
            assertThat(certification.getUser()).isEqualTo(user);
            assertThat(certification.getChallenge()).isEqualTo(challenge);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("챌린지 인증 이미지 링크")
            class CreatedDate {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    imgLink = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("첨부된 파일이 없습니다.");
                }

                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    imgLink = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("첨부된 파일이 없습니다.");
                }

                @Test
                @DisplayName("URL")
                void fail_url_form() {
                    // given
                    imgLink = "test_imgLink";
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("이미지 링크를 확인해주세요.");
                }
            }

            @Nested
            @DisplayName("챌린지 인증 내용")
            class Contents {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    contents = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("인증 내용이 없습니다.");
                }

                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    contents = "";
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("인증 내용이 없습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 인증 유저")
            class User {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    user = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("유저 정보가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("인증 챌린지")
            class Challenge {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    challenge = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Certification(imgLink, contents, user, challenge));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 정보가 유효하지 않습니다.");
                }
            }
        }
    }
}