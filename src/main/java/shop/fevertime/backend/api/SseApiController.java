package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.fevertime.backend.service.NotificationService;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SseApiController {

    private final NotificationService notificationService;

    @CrossOrigin
    @GetMapping(value = "/sub/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        return notificationService.subscribe(userId);
    }
}
