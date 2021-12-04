package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Certification;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.CertificationRequestDto;
import shop.fevertime.backend.dto.response.CertificationResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.repository.CertificationRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final S3Uploader s3Uploader;
    private final ChallengeRepository challengeRepository;

    public List<CertificationResponseDto> getCertifications(Long challengeId) {
        return certificationRepository.findAllByChallengeId(challengeId)
                .stream()
                .map(CertificationResponseDto::new)
                .collect(Collectors.toList());
    }

    public CertificationResponseDto getCertification(Long certiId) {
        return certificationRepository.findById(certiId)
                .map(CertificationResponseDto::new)
                .orElseThrow(
                        () -> new NoSuchElementException("존재하지 않는 인증입니다.")
                );

    }

    @Transactional
    public void createCertification(Long challengeId, CertificationRequestDto requestDto, User user) throws IOException {

        // 이미지 AWS S3 업로드
        String uploadImageUrl = s3Uploader.upload(requestDto.getImage(), "certification");

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다."));

        // 인증 생성
        Certification certification = new Certification(
                uploadImageUrl,
                requestDto.getContents(),
                user,
                challenge
        );
        certificationRepository.save(certification);
    }

    public void deleteCertification(Long certiId) {
        //이미지 s3에서 삭제
        CertificationResponseDto responseDto = certificationRepository.findById(certiId)
                .map(CertificationResponseDto::new)
                .orElseThrow(
                        () -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        String[] ar = responseDto.getImgLink().split("/");
        s3Uploader.delete(ar[ar.length - 1], "certification");

        certificationRepository.deleteById(certiId);
    }

    public ResultResponseDto checkCertificationCreator(Long certiId, User user) {
        boolean present = certificationRepository.findByIdAndUser(certiId, user).isPresent();
        if (present) {
            return new ResultResponseDto("success", "챌린지 인증 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "챌린지 인증 생성자가 아닙니다.");
    }
}
