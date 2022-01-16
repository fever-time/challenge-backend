package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import shop.fevertime.backend.dto.ChatMessageDto;
import shop.fevertime.backend.dto.MessageDto;

@Controller
@RequiredArgsConstructor
public class ChatApiController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void addUser(@RequestBody ChatMessageDto messageDto) {
        if (ChatMessageDto.MessageType.ENTER.equals(messageDto.getType()))
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

}