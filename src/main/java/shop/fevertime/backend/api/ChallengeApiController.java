package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    //카테고리에 해당하는 챌린지 리스트 가져오기
    @GetMapping("/challenges")
    public List<ChallengeResponseDto> getChallenges(@RequestParam("kind") String category) {
        return challengeService.getChallenges(category);
    }

    @GetMapping("/challenges/{challengeId}")
    public ChallengeResponseDto getChallenge(@PathVariable Long challengeId) {
        return challengeService.getChallenge(challengeId);
    }

    //새로운 챌린지 생성
    @PostMapping("/challenges")
    public String createChallenge(
            @ModelAttribute ChallengeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        challengeService.createChallenge(requestDto, userDetails.getUser());
        return "ok";
    }

    //특정 챌린지 삭제
    @DeleteMapping("/challenges/{challengeId}")
    public String deleteChallenge(@PathVariable Long challengeId) {
        challengeService.deleteChallenge(challengeId);
        return "ok";
    }

    //검색어에 해당하는 챌린지 리스트 가져오기
    @GetMapping("/search")
    public List<ChallengeResponseDto> searchChallenges(@RequestParam("search") String search) {
        return challengeService.searchChallenges(search);
    }
}
