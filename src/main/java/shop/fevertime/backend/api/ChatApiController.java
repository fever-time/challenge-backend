package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import shop.fevertime.backend.dto.ChatMessageDto;

@Controller
@RequiredArgsConstructor
public class ChatApiController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto messageDto){

        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

    @MessageMapping("/chat/exit")
    public void exit(ChatMessageDto messageDto) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

}