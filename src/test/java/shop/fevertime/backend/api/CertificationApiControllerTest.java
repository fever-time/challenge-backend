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
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.CertificationRepository;
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
class CertificationApiControllerTest {

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
    @Autowired
    CertificationRepository certificationRepository;

    private Long challengeId;
    private Long certiId;

    @BeforeAll
    void init() {
        User test = new User("test", "tes@email.com", UserRole.USER, "321", "https://img.com/img");
        this.userRepository.save(test);

        Category category = new Category("??????");
        this.categoryRepository.save(category);

        Challenge challenge = new Challenge("??????", "??????", "https://img.com/img", LocalDateTime.now(), LocalDateTime.now(), 10, LocationType.ONLINE, "", test, category, ChallengeProgress.INPROGRESS);
        this.challengeRepository.save(challenge);
        challengeId = challenge.getId();

        Certification certification = new Certification("https://img.com/img", "??????", test, challenge);
        certificationRepository.save(certification);
        certiId = certification.getId();
    }

    @AfterAll
    void clear() {
        certificationRepository.deleteAll();
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
    void ?????????_??????_?????????() throws Exception {
        mockMvc.perform(get("/challenges/{challengeId}/certis", challengeId))
                .andExpect(status().isOk())
                .andDo(document("certification/list",
                        pathParameters(
                                parameterWithName("challengeId").description("????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("[].certificationId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                                fieldWithPath("[].userId").type(JsonFieldType.STRING).description("?????? ID"),
                                fieldWithPath("[].imgUrl").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("[].contents").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                        )
                ));
    }

    @Test
    @Order(2)
    void ?????????_??????_????????????() throws Exception {
        mockMvc.perform(get("/challenges/{challengeId}/certis/{certiId}", challengeId, certiId))
                .andExpect(status().isOk())
                .andDo(document("certification/detail",
                        pathParameters(
                                parameterWithName("challengeId").description("????????? ID"),
                                parameterWithName("certiId").description("????????? ?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("certificationId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("?????? ID"),
                                fieldWithPath("imgUrl").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("contents").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                        )
                ));
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "321")
    void ?????????_??????() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png",
                "<<png data>>".getBytes());

        mockMvc.perform(fileUpload("/challenges/{challengeId}/certi", challengeId)
                .file(image)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=")
                .param("contents", "??????"))
                .andExpect(status().isOk())
                .andDo(document("certification/create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("?????? ??????")
                        ),
                        pathParameters(
                                parameterWithName("challengeId").description("????????? ID")
                        ),
                        requestParameters(
                                parameterWithName("contents").description("????????? ?????? ??????")
                        ),
                        requestParts(
                                partWithName("image").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("?????????")
                        )
                ));
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "321")
    void ?????????_??????_??????() throws Exception {
        mockMvc.perform(delete("/challenges/{challengeId}/certis/{certiId}", challengeId, certiId)
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ="))
                .andExpect(status().isOk())
                .andDo(document("certification/delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("?????? ??????")
                        ),
                        pathParameters(
                                parameterWithName("challengeId").description("????????? ID"),
                                parameterWithName("certiId").description("????????? ?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("?????????")
                        )
                ));
    }

}