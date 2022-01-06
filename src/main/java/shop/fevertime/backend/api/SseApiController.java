package shop.fevertime.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.fevertime.backend.service.NotificationService;

@Slf4j
@RestController
public class SseApiController {

    private final NotificationService notificationService;

    public SseApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @CrossOrigin
    @GetMapping(value = "/sub/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {
        return notificationService.subscribe(userId);
    }
}
