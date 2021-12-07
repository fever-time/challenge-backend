package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.fevertime.backend.util.LocalDateTimeUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

    @Nested
    @DisplayName("챌린지 객체 생성")
    class CreateChallenge {

        private String title;
        private String description;
        private String imgLink;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int limitPerson;
        private LocationType locationType;
        private String address;
        private User user;
        private Category category;

        @BeforeEach
        void setup() {

            title = "제목";
            description = "내용";
            imgLink = "";
            startDate = LocalDateTimeUtil.getLocalDateTime("2020-01-01");
            endDate = LocalDateTimeUtil.getLocalDateTime("2020-12-12");
            limitPerson = 10;
            locationType = LocationType.OFFLINE;
            address = "강남구";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "");
            category = new Category("운동");
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

            // then
            assertThat(challenge.getId()).isNull();
            assertThat(challenge.getTitle()).isEqualTo(title);
            assertThat(challenge.getDescription()).isEqualTo(description);
            assertThat(challenge.getImgLink()).isEqualTo(imgLink);
            assertThat(challenge.getStartDate()).isEqualTo(startDate);
            assertThat(challenge.getEndDate()).isEqualTo(endDate);
            assertThat(challenge.getLimitPerson()).isEqualTo(limitPerson);
            assertThat(challenge.getLocationType()).isEqualTo(locationType);
            assertThat(challenge.getAddress()).isEqualTo(address);
            assertThat(challenge.getUser()).isEqualTo(user);
            assertThat(challenge.getCategory()).isEqualTo(category);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("챌린지 제목")
            class Title {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    title = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    title = "";

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 설명")
            class Description {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    description = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    description = "";

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 이미지 링크")
            class ImgLink {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    imgLink = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    imgLink = "";

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
                @Test
                @DisplayName("URL")
                void fail_url_form() {
                    // given
                    imgLink = "";

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 시작 기간")
            class StartDate {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    startDate = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 종료 기간")
            class EndDate {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    endDate = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 제한 인원")
            class LimitPerson {

                @Test
                @DisplayName("음수")
                void fail_null() {
                    // given
                    limitPerson = -10;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
                @Test
                @DisplayName("적은 인원")
                void fail_lessLimit() {
                    // given
                    limitPerson = 1;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 참여위치 타입")
            class LocationType {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    locationType = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 오프라인 주소")
            class Address {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    address = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 개설자")
            class User {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    user = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }

            @Nested
            @DisplayName("챌린지 카테고리")
            class Category {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    category = null;

                    // when
                    Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);

                    // then
                }
            }
        }
    }
}