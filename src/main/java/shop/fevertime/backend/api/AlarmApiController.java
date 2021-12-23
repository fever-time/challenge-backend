package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import shop.fevertime.backend.dto.MessageDto;

@Controller
@RequiredArgsConstructor
public class AlarmApiController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/post")
    public void addUser(@RequestBody MessageDto dto) {
        messagingTemplate.convertAndSend("/topic/feed", dto);
    }

}