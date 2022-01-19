package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.dto.ChatMessageDto;
import shop.fevertime.backend.service.ChatRoomService;

@Controller
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto messageDto) {
        chatRoomService.enterRoom(messageDto);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto) {
        chatRoomService.sendMsg(messageDto);
    }

    @MessageMapping("/chat/exit")
    public void exit(ChatMessageDto messageDto) {
        chatRoomService.exitRoom(messageDto);
    }

}