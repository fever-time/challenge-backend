package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.ChallengeRequestDto;
import shop.fevertime.backend.dto.request.ChallengeUpdateRequestDto;
import shop.fevertime.backend.dto.response.ChallengeResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeApiController {

    private final ChallengeService challengeService;

    /**
     * 카테고리별 챌린지 조회 API
     */
    @GetMapping("/challenges")
    public List<ChallengeResponseDto> getChallenges(@RequestParam("kind") String category) {
        return challengeService.getChallenges(category);
    }

    /**
     * 챌린지 검색 API
     */
    @GetMapping("/challenges/search")
    public List<ChallengeResponseDto> searchChallenges(@RequestParam("search") String search) {
        return challengeService.searchChallenges(search);
    }

    /**
     * 챌린지 상세 조회 API
     */
    @GetMapping("/challenges/{challengeId}")
    public ChallengeResponseDto getChallenge(@PathVariable Long challengeId) {
        return challengeService.getChallenge(challengeId);
    }

    /**
     * 챌린지 생성 API
     */
    @PostMapping("/challenge")
    public ResultResponseDto createChallenge(
            @ModelAttribute ChallengeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.createChallenge(requestDto, userDetails.getUser());
        return new ResultResponseDto("success", "챌린지 생성되었습니다.");
    }

    /**
     * 챌린지 수정 API
     */
    @PutMapping("/challenges/{challengeId}")
    public ResultResponseDto updateChallenge(
            @PathVariable Long challengeId,
            @ModelAttribute ChallengeUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.updateChallenge(challengeId, requestDto, userDetails.getUser());
        return new ResultResponseDto("success", "챌린지 수정되었습니다.");
    }

    /**
     * 챌린지 삭제 API
     */
    @DeleteMapping("/challenges/{challengeId}")
    public ResultResponseDto deleteChallenge(@PathVariable Long challengeId) {
        challengeService.deleteChallenge(challengeId);
        return new ResultResponseDto("success", "챌린지 삭제되었습니다.");
    }
}
