package shop.fevertime.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;

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
class FeedApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeedRepository feedRepository;

    private Long feedId;

    @BeforeAll
    void init() {
        User test = new User("test", "tes@email.com", UserRole.USER, "12345", "https://img.com/img");
        this.userRepository.save(test);
        Feed feed = new Feed("생성", test);
        this.feedRepository.save(feed);
        feedId = feed.getId();
    }

    @AfterAll
    void clear() {
        feedRepository.deleteAll();
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
    void 피드_리스트_조회() throws Exception {
        mockMvc.perform(get("/feeds"))
                .andExpect(status().isOk())
                .andDo(document("feeds/list",
                        responseFields(
                                fieldWithPath("[].feedId").type(JsonFieldType.NUMBER).description("피드 ID"),
                                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("[].username").type(JsonFieldType.STRING).description("생성 유저 이름"),
                                fieldWithPath("[].imgLink").type(JsonFieldType.STRING).description("생성 유저 이미지"),
                                fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("피드 생성 날짜")
                        )
                ));
    }

    @Test
    @Order(2)
    @WithUserDetails(value = "12345")
    void 피드_생성() throws Exception {
        FeedRequestDto feedRequestDto = new FeedRequestDto("피드 생성");
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(feedRequestDto);

        mockMvc.perform(post("/feed")
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("feeds/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰"))
                        ,
                        requestFields(
                                fieldWithPath("contents").description("피드 내용")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "12345")
    void 피드_수정() throws Exception {
        FeedRequestDto feedRequestDto = new FeedRequestDto("피드 수정");
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(feedRequestDto);

        mockMvc.perform(put("/feeds/{feedId}", feedId)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("feeds/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("피드 내용")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "12345")
    void 피드_삭제() throws Exception {
        mockMvc.perform(delete("/feeds/{feedId}", feedId)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ="))
                .andExpect(status().isOk())
                .andDo(document("feeds/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지")
                        )
                ));
    }

}