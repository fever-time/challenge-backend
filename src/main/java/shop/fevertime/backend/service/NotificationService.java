package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.fevertime.backend.domain.Comment;
import shop.fevertime.backend.domain.Feed;
import shop.fevertime.backend.dto.MessageDto;
import shop.fevertime.backend.exception.ApiRequestException;
import shop.fevertime.backend.repository.CommentRepository;
import shop.fevertime.backend.repository.FeedRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class NotificationService {

    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    public final FeedRepository feedRepository;

    //댓글 달린 해당 피드 찾기 -> 피드 작성자 카카오아이디 조회
    public SseEmitter subscribe(String userId) {

        SseEmitter sseEmitter = new SseEmitter();
        try {
            // 연결!!
            sseEmitter.send(SseEmitter.event().id(userId).name("sse").data("연결완료!"));
        } catch (IOException e) {
            throw new ApiRequestException("연결 오류!");
        }

        // user의 pk값을 key값으로 해서 SseEmitter를 저장
        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }

    //댓글이 달리면 피드 작성자에게 알림 생성해서 보내기.
    @Transactional
    public void send(Long feedId) {
        //해당 피드 작성자 id 찾기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiRequestException("해당하는 피드가 없습니다.")
        );
        String userId = feed.getUser().getKakaoId();

        if (sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitters.get(userId);
            try {
                //데이터 전송
                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다!!!!!"));
            } catch (Exception e) {
                sseEmitters.remove(userId);
            }
        }
    }
}
