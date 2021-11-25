package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.domain.Challenge;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChallengeApiController {

    private final ChallengeService challengeService;

    @GetMapping("/challenges")
    public List<ChallengeResponseDto> getChallenges() {
        return challengeService.getChallenges();
    }

    @GetMapping("/challenges/{challengeId}")
    public ChallengeResponseDto getChallenge(@PathVariable Long challengeId){
        return challengeService.getChallenge(challengeId);
    }

    @PostMapping("/challenges")
    public String createChallenge(
            @ModelAttribute ChallengeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.createChallenge(requestDto, userDetails.getUser());
        return "ok";
    }

    @DeleteMapping("/challenges/{challengeId}")
    public String deleteChallenge(@PathVariable Long challengeId){
        challengeService.deleteChallenge(challengeId);
        return "ok";
    }

}