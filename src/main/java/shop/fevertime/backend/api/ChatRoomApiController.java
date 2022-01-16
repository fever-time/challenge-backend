package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.dto.ChatRoomDto;
import shop.fevertime.backend.dto.response.ResultResponseDto;
import shop.fevertime.backend.repository.ChatRoomRepository;
import shop.fevertime.backend.security.UserDetailsImpl;
import shop.fevertime.backend.service.ChatRoomService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomApiController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 목록 조회 API
     */
    //반환값 dto로 변경해야함
    @GetMapping("/rooms")
    public List<ChatRoom> getRooms() {
        return chatRoomService.getAllRooms();
    }

    /**
     * 채팅방 생성 API
     */
    @PostMapping("/room")
    public ResultResponseDto createRoom(@RequestBody ChatRoomDto chatRoomDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.createRoom(chatRoomDto, userDetails.getUser());
        return new ResultResponseDto("success", "채팅 방이 생성되었습니다.");
    }

    /**
     * 채팅방 삭제 API
     */
    @DeleteMapping("/room/{roomId}")
    public ResultResponseDto deleteRoom(@PathVariable Long roomId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.deleteRoom(roomId, userDetails.getUser());
        return new ResultResponseDto("success", "채팅 방이 삭제되었습니다.");
    }

}
