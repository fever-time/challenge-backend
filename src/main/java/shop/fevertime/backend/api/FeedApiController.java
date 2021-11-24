package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.service.FeedService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/feeds")
@RestController
public class FeedApiController {

    private final FeedService feedService;

    @GetMapping()
    public List<Feed> readFeeds() {
        return feedService.readFeeds();
    }

    @PostMapping()
    public Feed createFeed(@RequestBody FeedRequestDto requestDto) {
        return feedService.createFeed(requestDto);
    }

    @PutMapping("/{feedId}")
    public Long updateFeed(@PathVariable Long feedId, @RequestBody FeedRequestDto requestDto) {
        return feedService.updateFeed(feedId, requestDto);
    }

    @DeleteMapping("/{feedId}")
    public Long deleteFeed(@PathVariable Long feedId) {
        return feedService.deleteFeed(feedId);
    }


}
