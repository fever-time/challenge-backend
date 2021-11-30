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

    @GetMapping("/challenges/{challengeId}/certifications")
    public List<CertificationResponseDto> getCertification(
            @PathVariable Long challengeId
    ) {
        return certificationService.getCertification(challengeId);
    }

    @PostMapping("/certifications")
    public String createCertification(
            @ModelAttribute CertificationRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        certificationService.createCertification(requestDto, userDetails.getUser());
        return "ok";
    }

    @DeleteMapping("/certifications/{certificationId}")
    public String deleteCertification(@PathVariable Long certificationId) {
        certificationService.deleteCertification(certificationId);
        return "ok";
    }
}
