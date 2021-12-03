package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.FeedService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FeedApiController {

    private final FeedService feedService;

    /**
     * 피드 리스트 조회 API
     */
    @GetMapping("/feeds")
    public List<FeedResponseDto> getFeeds() {
        return feedService.getFeeds();
    }

    /**
     * 피드 생성 API
     */
    @PostMapping("/feed")
    public ResultResponseDto createFeed(@RequestBody FeedRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        feedService.createFeed(requestDto, userDetails.getUser());
        return new ResultResponseDto("success", "피드 생성되었습니다.");
    }

    /**
     * 피드 수정 API
     */
    @PutMapping("/feeds/{feedId}")
    public ResultResponseDto updateFeed(@PathVariable Long feedId, @RequestBody FeedRequestDto requestDto) {
        feedService.updateFeed(feedId, requestDto);
        return new ResultResponseDto("success", "피드 수정되었습니다.");
    }

    /**
     * 피드 삭제 API
     */
    @DeleteMapping("/feeds/{feedId}")
    public ResultResponseDto deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return new ResultResponseDto("success", "피드 삭제되었습니다.");
    }
}
