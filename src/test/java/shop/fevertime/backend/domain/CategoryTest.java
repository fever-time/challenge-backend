package shop.fevertime.backend.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Nested
    @DisplayName("카테고리 객체 생성")
    class CreateCategory {

        private String name;

        @BeforeEach
        void setup() {
            name = "운동";
        }

        @Test
        @DisplayName("정상 케이스")
        void create_Normal() {
            // given

            // when
            Category category = new Category(name);

            // then
            assertThat(category.getId()).isNull();
            assertThat(category.getName()).isEqualTo(name);
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {

            @Nested
            @DisplayName("카테고리 이름")
            class Name {

                @Test
                @DisplayName("null")
                void fail_null() {
                    // given
                    name = null;

                    // when
                    Category category = new Category(name);

                    // then
                }
                @Test
                @DisplayName("공백")
                void fail_empty() {
                    // given
                    name = "";

                    // when
                    Category category = new Category(name);

                    // then
                }
            }
        }
    }
}