package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.request.ChallengeUpdateRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChallengeApiController {
    /**
     * getChallenges: 카테고리별 챌린지 조회 API
     * searchChallenges: 챌린지 검색 API
     * getChallenge: 특정 챌린지 조회 API
     * createChallenge: 챌린지 생성 API
     * updateChallenge: 챌린지 수정 API
     * deleteChallenge: 챌린지 삭제 API
     */

    private final ChallengeService challengeService;

    @GetMapping("/challenges")
    public List<ChallengeResponseDto> getChallenges(@RequestParam("kind") String category) {
        return challengeService.getChallenges(category);
    }

    @GetMapping("/challenges/search")
    public List<ChallengeResponseDto> searchChallenges(@RequestParam("search") String search) {
        return challengeService.searchChallenges(search);
    }

    @GetMapping("/challenges/{challengeId}")
    public ChallengeResponseDto getChallenge(@PathVariable Long challengeId) {
        return challengeService.getChallenge(challengeId);
    }

    @PostMapping("/challenge")
    public String createChallenge(
            @ModelAttribute ChallengeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.createChallenge(requestDto, userDetails.getUser());
        return "ok";
    }

    @PutMapping("/challenges/{challengeId}")
    public String updateChallenge(
            @PathVariable Long challengeId,
            @ModelAttribute ChallengeUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.updateChallenge(challengeId, requestDto, userDetails.getUser());
        return "ok";
    }

    @DeleteMapping("/challenges/{challengeId}")
    public String deleteChallenge(@PathVariable Long challengeId) {
        challengeService.deleteChallenge(challengeId);
        return "ok";
    }
}
