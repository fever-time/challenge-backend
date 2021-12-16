package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.fevertime.backend.exception.ApiRequestException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeHistoryTest {

    @Nested
    @DisplayName("챌린지 참여기록 객체 생성")
    class CreateChallengeHistory {

        private User user;
        private Challenge challenge;
        private LocalDateTime createdDate;
        private LocalDateTime missionDate;
        private ChallengeStatus challengeStatus;

        @BeforeEach
        void setup() {
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://img.com/img");
            Category category = new Category("운동");
            challenge = new Challenge("제목", "설명", "https://img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.ONLINE, "", user, category, ChallengeProgress.INPROGRESS);
            createdDate = LocalDateTime.now();
            missionDate = LocalDateTime.now().plusDays(7);
            challengeStatus = ChallengeStatus.JOIN;
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            ChallengeHistory challengeHistory = new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus);
            // then
            assertThat(challengeHistory.getId()).isNull();
            assertThat(challengeHistory.getUser()).isEqualTo(user);
            assertThat(challengeHistory.getChallenge()).isEqualTo(challenge);
            assertThat(challengeHistory.getCreatedDate()).isEqualTo(createdDate);
            assertThat(challengeHistory.getMissionDate()).isEqualTo(missionDate);
            assertThat(challengeHistory.getChallengeStatus()).isEqualTo(challengeStatus);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("챌린지 참여 유저")
            class User {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    user = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("유저 Id 가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("참여 챌린지")
            class Challenge {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    challenge = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("참여 시간")
            class CreatedDate {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    createdDate = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 참가 날짜가 없습니다.");
                }
            }

            @Nested
            @DisplayName("미션 시간")
            class MissionDate {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    missionDate = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 미션 날짜가 없습니다.");
                }
            }

            @Nested
            @DisplayName("챌린지 참여 상태")
            class ChallengeStatus {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    challengeStatus = null;
                    // when
                    Exception exception = assertThrows(ApiRequestException.class,
                            () -> new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus));
                    // then
                    assertThat(exception.getMessage()).isEqualTo("챌린지 참여 상태가 없습니다.");
                }
            }
        }
    }

    @Nested
    @DisplayName("챌린지 참여기록 객체 수정")
    class Update {

        private User user;
        private Challenge challenge;
        private LocalDateTime createdDate;
        private LocalDateTime missionDate;
        private ChallengeStatus challengeStatus;

        @BeforeEach
        void setup() {
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://img.com/img");
            Category category = new Category("운동");
            challenge = new Challenge("제목", "설명", "https://img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.ONLINE, "", user, category, ChallengeProgress.INPROGRESS);
            createdDate = LocalDateTime.now();
            missionDate = LocalDateTime.now().plusDays(7);
            challengeStatus = ChallengeStatus.JOIN;
        }

        @Test
        @DisplayName("정상 케이스_취소")
        void update_Normal1() {
            // given
            ChallengeHistory challengeHistory = new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus);
            // when
            challengeHistory.cancel();
            // then
            assertThat(challengeHistory.getId()).isNull();
            assertThat(challengeHistory.getUser()).isEqualTo(user);
            assertThat(challengeHistory.getChallenge()).isEqualTo(challenge);
            assertThat(challengeHistory.getCreatedDate()).isEqualTo(createdDate);
            assertThat(challengeHistory.getMissionDate()).isEqualTo(missionDate);
            assertThat(challengeHistory.getChallengeStatus()).isEqualTo(ChallengeStatus.CANCEL);
        }

        @Test
        @DisplayName("정상 케이스_미션실패")
        void update_Normal2() {
            // given
            ChallengeHistory challengeHistory = new ChallengeHistory(user, challenge, createdDate, missionDate, challengeStatus);
            // when
            challengeHistory.fail();
            // then
            assertThat(challengeHistory.getId()).isNull();
            assertThat(challengeHistory.getUser()).isEqualTo(user);
            assertThat(challengeHistory.getChallenge()).isEqualTo(challenge);
            assertThat(challengeHistory.getCreatedDate()).isEqualTo(createdDate);
            assertThat(challengeHistory.getMissionDate()).isEqualTo(missionDate);
            assertThat(challengeHistory.getChallengeStatus()).isEqualTo(ChallengeStatus.FAIL);
        }
    }
}