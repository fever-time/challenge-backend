package shop.fevertime.backend.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.dto.request.CertificationRequestDto;
import shop.fevertime.backend.dto.response.CertificationResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;
import shop.fevertime.backend.service.CertificationService;
import shop.fevertime.backend.util.LocalDateTimeUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CertificationIntegrationTest {

    @Autowired
    CertificationService certificationService;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

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
        private final CertificationRequestDto requestDto1 = new CertificationRequestDto();
        private final CertificationRequestDto requestDto2 = new CertificationRequestDto();

        @BeforeEach
        void setup() {
            title = "제목";
            description = "내용";
            imgLink = "https://www.img.com/img";
            startDate = LocalDateTimeUtil.getLocalDateTime("2020-01-01");
            endDate = LocalDateTimeUtil.getLocalDateTime("2020-12-12");
            limitPerson = 10;
            locationType = LocationType.OFFLINE;
            address = "강남구";
            user = new User("test", "test@email.com", UserRole.USER, "123456", "https://www.img.com/img");
            userRepository.save(user);
            category = new Category("운동");
            categoryRepository.save(category);


            byte[] content = new byte[0];
            MultipartFile image = new MockMultipartFile("content", "img.png", "multipart/mixed", content);

            requestDto1.setContents("첫번째 인증");
            requestDto1.setImage(image);

            requestDto2.setContents("두번째 인증");
            requestDto2.setImage(image);
        }

        @Test
        @Order(1)
        @DisplayName("인증 생성")
        void createCerti() throws IOException {
            //given
            Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);
            challengeRepository.save(challenge);

            //when
            certificationService.createCertification(challenge.getId(), requestDto1, user);
            certificationService.createCertification(challenge.getId(), requestDto2, user);

            //then
            List<Certification> allByChallenge = certificationRepository.findAllByChallenge(challenge);
            Certification certification = allByChallenge.get(0);

            assertNotNull(certification.getId());
            assertEquals(certification.getContents(),"첫번째 인증");
        }

        @Test
        @Order(2)
        @Transactional
        @DisplayName("특정 챌린지 인증 조회")
        void getCerti() throws IOException {
            //given
            Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);
            challengeRepository.save(challenge);

            certificationService.createCertification(challenge.getId(), requestDto1, user);
            certificationService.createCertification(challenge.getId(), requestDto2, user);

            //when
            List<CertificationResponseDto> certifications = certificationService.getCertifications(challenge.getId());

            //then
            assertThat(certifications.size()).isEqualTo(2);
        }

        @Test
        @Order(3)
        @DisplayName("인증 삭제")
        void deleteCerti() throws IOException {
            //given
            Challenge challenge = new Challenge(title, description, imgLink, startDate, endDate, limitPerson, locationType, address, user, category);
            challengeRepository.save(challenge);

            certificationService.createCertification(challenge.getId(), requestDto1, user);
            certificationService.createCertification(challenge.getId(), requestDto2, user);
            List<Certification> allByChallenge = certificationRepository.findAllByChallenge(challenge);
            Certification certification = allByChallenge.get(0);

            //생성자 확인
            ResultResponseDto resultResponseDto = certificationService.checkCertificationCreator(certification.getId(), user);
            assertThat(resultResponseDto.getMsg()).isEqualTo("챌린지 인증 생성자가 맞습니다.");

            //when
            certificationService.deleteCertification(certification.getId(),user);

            //then
            List<Certification> allByChallenge1 = certificationRepository.findAllByChallenge(challenge);
            assertThat(allByChallenge1.size()).isEqualTo(1);
        }
    }
}
