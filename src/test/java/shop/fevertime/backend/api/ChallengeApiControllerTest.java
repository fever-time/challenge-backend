package shop.fevertime.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest()
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChallengeApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ChallengeRepository challengeRepository;

    private Long challengeId;

    @BeforeAll
    void init() {
        User test = new User("test", "tes@email.com", UserRole.USER, "456", "https://img.com/img");
        this.userRepository.save(test);

        Category category = new Category("운동");
        this.categoryRepository.save(category);

        Challenge challenge = new Challenge("title", "설명", "https://img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.ONLINE, "", test, category);
        this.challengeRepository.save(challenge);

        challengeId = challenge.getId();
    }

    @AfterAll
    void clear() {
        challengeRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Order(1)
    void 카테고리별_챌린지_조회() throws Exception {
        mockMvc.perform(get("/challenges")
                .param("kind", "All"))
                .andExpect(status().isOk())
                .andDo(document("challenges/list",
                        requestParameters(
                                parameterWithName("kind").description("카테고리")
                        ),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING).description("챌린지 상세 설명"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("챌린지 시작 날짜"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("챌린지 마감 날짜"),
                                fieldWithPath("[].limitPerson").type(JsonFieldType.NUMBER).description("챌린지 제한 인원"),
                                fieldWithPath("[].locationType").type(JsonFieldType.STRING).description("챌린지 장소 타입"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("챌린지 주소"),
                                fieldWithPath("[].imgLink").type(JsonFieldType.STRING).description("챌린지 이미지 링크"),
                                fieldWithPath("[].category.name").type(JsonFieldType.STRING).description("챌린지 카테고리"),
                                fieldWithPath("[].participants").type(JsonFieldType.NUMBER).description("챌린지 참여자수")
                        )
                ));
    }

    @Test
    @Order(2)
    void 챌린지_검색() throws Exception {
        mockMvc.perform(get("/challenges/search")
                .param("search", "title"))
                .andExpect(status().isOk())
                .andDo(document("challenges/search",
                        requestParameters(
                                parameterWithName("search").description("챌린지 검색 제목")
                        ),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING).description("챌린지 상세 설명"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("챌린지 시작 날짜"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("챌린지 마감 날짜"),
                                fieldWithPath("[].limitPerson").type(JsonFieldType.NUMBER).description("챌린지 제한 인원"),
                                fieldWithPath("[].locationType").type(JsonFieldType.STRING).description("챌린지 장소 타입"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("챌린지 주소"),
                                fieldWithPath("[].imgLink").type(JsonFieldType.STRING).description("챌린지 이미지 링크"),
                                fieldWithPath("[].category.name").type(JsonFieldType.STRING).description("챌린지 카테고리"),
                                fieldWithPath("[].participants").type(JsonFieldType.NUMBER).description("챌린지 참여자수")
                        )
                ));
    }

    @Test
    @Order(3)
    void 챌린지_상세_조회() throws Exception {
        mockMvc.perform(get("/challenges/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andDo(document("challenges/detail",
                        pathParameters(
                                parameterWithName("challengeId").description("챌린지 ID")
                        ),
                        responseFields(
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("챌린지 상세 설명"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("챌린지 시작 날짜"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("챌린지 마감 날짜"),
                                fieldWithPath("limitPerson").type(JsonFieldType.NUMBER).description("챌린지 제한 인원"),
                                fieldWithPath("locationType").type(JsonFieldType.STRING).description("챌린지 장소 타입"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("챌린지 주소"),
                                fieldWithPath("imgLink").type(JsonFieldType.STRING).description("챌린지 이미지 링크"),
                                fieldWithPath("category.name").type(JsonFieldType.STRING).description("챌린지 카테고리"),
                                fieldWithPath("participants").type(JsonFieldType.NUMBER).description("챌린지 참여자수")
                        )
                ));
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "456")
    void 챌린지_생성() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png",
                "<<png data>>".getBytes());

        mockMvc.perform(fileUpload("/challenge")
                .file(image)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=")
                .param("title", "title")
                .param("description", "description")
                .param("startDate", "2021-01-01")
                .param("endDate", "2022-12-12")
                .param("limitPerson", "10")
                .param("locationType", "ONLINE")
                .param("category", "운동")
                .param("address", ""))
                .andExpect(status().isOk())
                .andDo(document("challenges/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰")
                        ),
                        requestParameters(
                                parameterWithName("title").description("제목"),
                                parameterWithName("description").description("내용"),
                                parameterWithName("startDate").description("챌린지 시작 날짜"),
                                parameterWithName("endDate").description("챌린지 마감 날짜"),
                                parameterWithName("limitPerson").description("제한인원"),
                                parameterWithName("locationType").description("챌린지 진행 장소"),
                                parameterWithName("category").description("챌린지 카텔고리"),
                                parameterWithName("address").description("챌린지 오프라인 주소")
                        ),
                        requestParts(
                                partWithName("image").description("이미지")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }

    @Test
    @Order(5)
    @WithUserDetails(value = "456")
    void 챌린지_수정() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png",
                "<<png data>>".getBytes());
        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.fileUpload("/challenges/{challengeId}", challengeId);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(image)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=")
                .param("address", ""))
                .andExpect(status().isOk())
                .andDo(document("challenges/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("challengeId").description("챌린지 ID")
                        ),
                        requestParameters(
                                parameterWithName("address").description("챌린지 오프라인 주소")
                        ),
                        requestParts(
                                partWithName("image").description("이미지")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }

    @Test
    @Order(6)
    @WithUserDetails(value = "456")
    void 챌린지_삭제() throws Exception {
        mockMvc.perform(delete("/challenges/{challengeId}", challengeId)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ="))
                .andExpect(status().isOk())
                .andDo(document("challenges/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("challengeId").description("챌린지 ID")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }
}