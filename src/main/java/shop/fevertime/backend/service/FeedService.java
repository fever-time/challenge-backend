package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public List<Feed> readFeeds() {
        return feedRepository.findAll();
    }

    @Transactional
    public Feed createFeed(FeedRequestDto requestDto) {
        Feed feed = new Feed(requestDto);
        return feedRepository.save(feed);
    }

    @Transactional
    public Long updateFeed(Long id, FeedRequestDto requestDto) {
        Feed feed = feedRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하는 댓글이 없습니다.")
        );
        feed.update(requestDto);
        return id;
    }

    @Transactional
    public Long deleteFeed(Long id) {
        feedRepository.deleteById(id);
        return id;
    }
}
