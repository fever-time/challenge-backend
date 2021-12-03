package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.response.ChallengeUserResponseDto;
import shop.fevertime.backend.dto.response.UserCertifiesResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges/{challengeId}")
public class ChallengeHistoryApiController {

    private final ChallengeHistoryService challengeHistoryService;

    @GetMapping("/users")
    public List<UserCertifiesResponseDto> getChallengeHistoryUsers(
            @PathVariable Long challengeId
    ) {
        return challengeHistoryService.getChallengeHistoryUsers(challengeId);
    }

    @GetMapping("/user")
    public ChallengeUserResponseDto getChallengeHistoryUser(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return challengeHistoryService.getChallengeHistoryUser(challengeId, userDetails.getUser());
    }

    @PostMapping("/join")
    public String joinChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.joinChallenge(challengeId, userDetails.getUser());
        return "ok";
    }

    @PutMapping("/join")
    public String cancelChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.cancelChallenge(challengeId, userDetails.getUser());
        return "ok";
    }
}
