package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeHistoryService;

@RestController
@RequiredArgsConstructor
public class ChallengeHistoryApiController {

    private final ChallengeHistoryService challengeHistoryService;

    @PostMapping("/challenges/{challengeId}/join")
    public String joinChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.joinChallenge(challengeId, userDetails.getUser());
        return "ok";
    }

    @PutMapping("/challenges/{challengeId}/join")
    public String cancelChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.cancelChallenge(challengeId, userDetails.getUser());
        return "ok";
    }
}
