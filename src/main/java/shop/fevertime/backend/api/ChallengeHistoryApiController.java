package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.response.ChallengeUserResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.dto.response.UserCertifiesResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChallengeHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges/{challengeId}")
public class ChallengeHistoryApiController {

    private final ChallengeHistoryService challengeHistoryService;

    /**
     * 해당 챌린지 참여한 유저정보(+챌린지 인증 리스트) 리스트 API
     */
    @GetMapping("/users")
    public List<UserCertifiesResponseDto> getChallengeHistoryUsers(
            @PathVariable Long challengeId
    ) {
        return challengeHistoryService.getChallengeHistoryUsers(challengeId);
    }

    /**
     * 해당 챌린지 참여 기록 리스트
     */
    @GetMapping("/user")
    public ChallengeUserResponseDto getChallengeHistoryUser(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return challengeHistoryService.getChallengeHistoryUser(challengeId, userDetails.getUser());
    }

    /**
     * 챌린지 참여 API
     */
    @PostMapping("/join")
    public ResultResponseDto joinChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.joinChallenge(challengeId, userDetails.getUser());
        return new ResultResponseDto("success", "챌린지 참여되었습니다.");
    }

    /**
     * 챌린지 참여 취소 API
     */
    @PutMapping("/join")
    public ResultResponseDto cancelChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        challengeHistoryService.cancelChallenge(challengeId, userDetails.getUser());
        return new ResultResponseDto("success", "챌린지 참여 취소되었습니다.");
    }
}
