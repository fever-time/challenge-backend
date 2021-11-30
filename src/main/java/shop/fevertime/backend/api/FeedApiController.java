package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.FeedService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/feeds")
@RestController
public class FeedApiController {

    private final FeedService feedService;

    // 피드 조회
    @GetMapping()
    public List<FeedResponseDto> getFeeds() {
        return feedService.getFeeds();
    }

    // 피드 생성
    @PostMapping()
    public String createFeed(@RequestBody FeedRequestDto requestDto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        feedService.createFeed(requestDto, userDetails.getUser());
        return "ok";
    }

    // 피드 수정
    @PutMapping("/{feedId}")
    public String updateFeed(@PathVariable Long feedId, @RequestBody FeedRequestDto requestDto) {
        feedService.updateFeed(feedId, requestDto);
        return "ok";
    }

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public String deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return "ok";
    }
}
