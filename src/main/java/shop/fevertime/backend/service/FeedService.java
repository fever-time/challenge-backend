package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.request.FeedRequestDto;
import shop.fevertime.backend.dto.response.FeedResponseDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
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
        feedRepository.save(new Feed(requestDto.getContents(), user));
    }

    @Transactional
    public ResultResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user) {
        Feed feed = feedRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        feed.update(requestDto.getContents());
        return new ResultResponseDto("success", "피드 수정되었습니다.");
    }

    @Transactional
    public void deleteFeed(Long feedId, User user) {
        // 피드 삭제전에 댓글테이블에 feedId으로 댓글 삭제 추가
        Feed feed = feedRepository.findByIdAndUser(feedId, user).orElseThrow(
                () -> new ApiRequestException("피드가 존재하지 않거나 삭제 권한이 없습니다.")
        );
        commentRepository.deleteAllByFeed(feed);

        feedRepository.delete(feed);
    }

    public ResultResponseDto checkFeedCreator(Long feedId, User user) {
        boolean present = feedRepository.findByIdAndUser(feedId, user).isPresent();

        if (present) {
            return new ResultResponseDto("success", "피드 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "피드 생성자가 아닙니다.");
    }
}
