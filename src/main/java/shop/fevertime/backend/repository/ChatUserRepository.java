package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.domain.ChatUser;
import shop.fevertime.backend.domain.User;

import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    int countByChatRoom(ChatRoom chatRoom);
}
