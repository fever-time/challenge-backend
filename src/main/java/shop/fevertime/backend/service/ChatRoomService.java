package shop.fevertime.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.domain.User;
import shop.fevertime.backend.dto.ChatRoomDto;
import shop.fevertime.backend.repository.ChatRoomRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    //반환값 dto로 바꿔야함
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    @Transactional
    public void createRoom(ChatRoomDto chatRoomDto, User user) {
        chatRoomRepository.save(new ChatRoom(chatRoomDto, user));
    }

    @Transactional
    public void deleteRoom(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndUser(roomId, user);
        chatRoomRepository.delete(chatRoom);
    }

}
