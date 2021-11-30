package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.CertificationRequestDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.CertificationService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CertificationApiController {

    private final CertificationService certificationService;

    //챌린지에 인증 생성하기
    @PostMapping("/certifications")
    public String setCertification(
            @ModelAttribute CertificationRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        certificationService.setCertification(requestDto, userDetails.getUser());
        return "ok";
    }

    //특정 인증 삭제하기
    @DeleteMapping("/certifications/{certificationId}")
    public String deleteCertification(@PathVariable Long certificationId) {
        certificationService.deleteCertification(certificationId);
        return "ok";
    }
}
