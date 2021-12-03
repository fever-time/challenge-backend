package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    // 피드 조회
    public List<FeedResponseDto> getFeeds() {
        return feedRepository.findAll()
                .stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    // 피드 생성
    @Transactional // return 값 Long으로 통일
    public void createFeed(FeedRequestDto requestDto, User user) {
        feedRepository.save(new Feed(requestDto, user));
    }

    @Transactional
    public Long updateFeed(Long id, FeedRequestDto requestDto) {
        Feed feed = feedRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("존재하는 피드가 없습니다.")
        );
        feed.update(requestDto);
        return id;
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        // 피드 삭제전에 댓글테이블에 feedId으로 댓글 삭제 추가
        commentRepository.deleteAllByFeedId(feedId);
        feedRepository.deleteById(feedId);
    }

    public ResultResponseDto checkFeedCreator(Long feedId, String kakaoId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 피드입니다.")
        );

        if (!Objects.equals(feed.getUser().getKakaoId(), kakaoId)) {
            return new ResultResponseDto("fail", "피드 생성자가 아닙니다.");
        }
        return new ResultResponseDto("success", "피드 생성자가 맞습니다.");
    }
}
