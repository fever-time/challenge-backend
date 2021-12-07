package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.fevertime.backend.exception.ApiRequestException;
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
            imgLink = "anyImg.jpg"; // 프론트랑 상의해야함
            startDate = LocalDateTimeUtil.getLocalDateTime("2020-01-01");
            endDate = LocalDateTimeUtil.getLocalDateTime("2020-12-12");
            limitPerson = 10;
            locationType = LocationType.OFFLINE;
            address = "강남구";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://fever-prac.s3.ap-northeast-2.amazonaws.com/challenge/ee87a2ad-d03e-4e28-9a3f-609deda36cc0%EC%B6%94%EC%B9%B4%EC%B6%94%EC%B9%B4%EB%A7%81.png");
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
                @DisplayName("챌린지의 제목이 null이면 예외 발생")
                void title_null() {
                    // given
                    title = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 이름이 없습니다.");
                }

                @Test
                @DisplayName("챌린지가 공백이면 예외 발생")
                void title_empty() {
                    // given
                    title = "";

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 이름이 없습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 설명이 null이면 예외 발생")
            class Description {

                @Test
                @DisplayName("null")
                void description_null() {
                    // given
                    description = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 상세 설명이 없습니다.");

                }
                @Test
                @DisplayName("챌린지 설명이 공백이면 예외 발생")
                void description_empty() {
                    // given
                    description = "";

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 상세 설명이 없습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 이미지 링크가 null이면 예외 발생")
            class ImgLink {

                @Test
                @DisplayName("null")
                void imgLink_null() {
                    // given
                    imgLink = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("첨부된 파일이 없습니다.");
                }

                @Test
                @DisplayName("챌린지 이미지 링크가 공백이면 예외 발생")
                void imgLink_empty() {
                    // given
                    imgLink = "";

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("첨부된 파일이 없습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 시작 기간")
            class StartDate {

                @Test
                @DisplayName("챌린지 시작 기간이 null이면 예외 발생")
                void startDate_null() {
                    // given
                    startDate = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("날짜 형식이 알맞지 않습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 종료 기간")
            class EndDate {

                @Test
                @DisplayName("챌린지 종료 기간이 null이면 예외 발생")
                void endDate_null() {
                    // given
                    endDate = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("날짜 형식이 알맞지 않습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 제한 인원")
            class LimitPerson {

                @Test
                @DisplayName("챌린지 신청 인원이 33명 초과하면 예외 발생")
                void applier_over() {
                    // given
                    limitPerson = 34;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("제한 인원은 1명~33명 사이로 입력하세요");
                }

                @Test
                @DisplayName("챌린지 신청 인원이 1명 미만일 때 예외가 발생")
                void applier_less() {
                    // given
                    limitPerson = 0;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("제한 인원은 1명~33명 사이로 입력하세요");
                }
            }

            @Nested
            @DisplayName("챌린지 참여위치 타입")
            class LocationType {

                @Test
                @DisplayName("챌린지 참여자 위치가 null이면 예외 발생")
                void locationType_null() {
                    // given
                    locationType = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 타입을 입력하세요.");
                }
            }

            @Nested
            @DisplayName("챌린지 오프라인 주소")
            class Address {

                @Test
                @DisplayName("챌린지 오프라인 주소가 null이면 예외 발생")
                void address_null() {
                    // given
                    address = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("오프라인인 경우 주소를 입력하세요.");
                }

                @Test
                @DisplayName("온라인 챌린지일 경우 address 입력시 예외 발생")
                void online_challenge_no_address() {
                    // given
                    locationType = shop.fevertime.backend.domain.LocationType.ONLINE;
                    address = "판교역";

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("온라인 챌린지는 주소를 설정할 수 없습니다.");
                }

            }

            @Nested
            @DisplayName("챌린지 개설자")
            class User {

                @Test
                @DisplayName("챌린지 개설자가 null이면 예외 발생")
                void challenge_creator_null() {
                    // given
                    user = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("유저 Id 가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 카테고리")
            class Category {

                @Test
                @DisplayName("챌린지 카테고리가 null이면 예외 발생")
                void category_null() {
                    // given
                    category = null;

                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category));

                    // then
                    assertThat(exception.getMessage()).isEqualTo("카테고리를 지정해주세요.");
                }
            }
        }
    }
}