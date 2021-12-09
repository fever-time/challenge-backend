package shop.fevertime.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.domain.UserRole;
import shop.fevertime.backend.dto.request.CommentRequestDto;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;
import shop.fevertime.backend.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeedRepository feedRepository;
    @Autowired
    CommentRepository commentRepository;

    private Long feedId;
    private Long commentId;

    @BeforeAll
    public void setData() {
        User test = new User("test", "tes@email.com", UserRole.USER, "123456", "https://img.com/img");
        this.userRepository.save(test);
        Feed feed = new Feed("생성", test);
        this.feedRepository.save(feed);
        feedId = feed.getId();

        Comment comment = new Comment(feed, "댓글 init", test);
        commentRepository.save(comment);
        commentId = comment.getId();
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Order(1)
    public void 댓글_조회() throws Exception {
        mockMvc.perform(get("/feeds/{feedId}/comments", feedId))
                .andExpect(status().isOk())
                .andDo(document("comment/list",
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID").optional(),
                                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("내용").optional(),
                                fieldWithPath("[].username").type(JsonFieldType.STRING).description("생성 유저 이름").optional(),
                                fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("피드 생성 날짜").optional()
                        )
                ));
    }

    @Test
    @Order(2)
    @WithUserDetails(value = "123456")
    public void 댓글_생성() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 생성");
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(commentRequestDto);

        mockMvc.perform(post("/feeds/{feedId}/comment", feedId)
                .header("Authorization", "Basic dXNlcjpzZWNyZXQ=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("comment/create",
                        requestHeaders(
                                headerWithName("Authorization").description("유저 토큰"))
                        ,
                        requestFields(
                                fieldWithPath("contents").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과").optional(),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지").optional()
                        )
                ));
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "123456")
    public void 댓글_수정() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 수정");
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(commentRequestDto);

        mockMvc.perform(put("/feeds/{feedId}/comments/{commentId}", feedId, commentId)
                .header("Authorization", "Basic dXNlcjpzZWNyZXQ=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("comment/update",
                        requestHeaders(
                                headerWithName("Authorization").description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과").optional(),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지").optional()
                        )
                ));
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "123456")
    public void 댓글_삭제() throws Exception {
        mockMvc.perform(delete("/feeds/{feedId}/comments/{commentId}", feedId, commentId)
                .header("Authorization", "Basic dXNlcjpzZWNyZXQ="))
                .andExpect(status().isOk())
                .andDo(document("comment/delete",
                        requestHeaders(
                                headerWithName("Authorization").description("유저 토큰"))
                        ,
                        pathParameters(
                                parameterWithName("feedId").description("피드 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("실행결과").optional(),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지").optional()
                        )
                ));
    }

}