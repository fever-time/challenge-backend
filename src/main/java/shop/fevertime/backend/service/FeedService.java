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
        if (requestDto.getContents().trim().length() == 0) {
            throw new ApiRequestException("공백으로 피드를 작성할 수 없습니다.");
        } else {
            feedRepository.save(new Feed(requestDto, user));
        }
    }

    @Transactional
    public ResultResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user) {
        Feed feed = feedRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 피드입니다.")
        );
        feed.update(requestDto);
        return new ResultResponseDto("success", "피드 수정되었습니다.");
    }

    @Transactional
    public void deleteFeed(Long feedId, User user) {
        // 피드 삭제전에 댓글테이블에 feedId으로 댓글 삭제 추가
        feedRepository.findByIdAndUser(feedId, user).orElseThrow(
                () -> new ApiRequestException("피드가 존재하지 않거나 삭제 권한이 없습니다.")
        );
        commentRepository.deleteAllByFeedId(feedId);

        feedRepository.deleteByIdAndUser(feedId, user).orElseThrow(
                () -> new ApiRequestException("정상적으로 삭제되지 않았습니다.")
        );

        /**
         * 1. 55번 라인을 처리해주지 않으면, 없는 피드일 때도 삭제가 되었다는 메시지가 나타난다.
         *      ispresent로 했을 때 디버깅을 했는데 true 반환..
         * 2. 그렇다면 61번에 대한 예외처리는 불필요하다고 생각한다.
         *      -> 55번 라인에서 user를 확인하기 때문에
         */
    }

    public ResultResponseDto checkFeedCreator(Long feedId, User user) {
        boolean present = feedRepository.findByIdAndUser(feedId, user).isPresent();

        if (present) {
            return new ResultResponseDto("success", "피드 생성자가 맞습니다.");
        }
        return new ResultResponseDto("fail", "피드 생성자가 아닙니다.");
    }
}
