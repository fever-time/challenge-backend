package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.CertificationRequestDto;
import shop.fevertime.backend.dto.response.CertificationResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.CertificationService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CertificationApiController {

    private final CertificationService certificationService;

    /**
     * 해당 챌린지의 인증 리스트 API
     */
    @GetMapping("/challenges/{challengeId}/certis")
    public List<CertificationResponseDto> getCertifications(
            @PathVariable Long challengeId
    ) {
        return certificationService.getCertifications(challengeId);
    }

    /**
     * 인증 상세정보 API
     */
    @GetMapping("/challenges/{challengeId}/certis/{certiId}")
    public CertificationResponseDto getCertificationOne(@PathVariable Long certiId) {
        return certificationService.getCertification(certiId);
    }

    /**
     * 챌린지 인증 API
     */
    @PostMapping("/challenges/{challengeId}/certi")
    public String createCertification(
            @PathVariable Long challengeId,
            @ModelAttribute CertificationRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        certificationService.createCertification(challengeId, requestDto, userDetails.getUser());
        return "ok";
    }

    /**
     * 챌린지 인증 삭제 API
     */
    @DeleteMapping("/challenges/{challengeId}/certis/{certiId}")
    public String deleteCertification(@PathVariable Long certiId) {
        certificationService.deleteCertification(certiId);
        return "ok";
    }
}
