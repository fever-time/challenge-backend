package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.domain.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByIdAndUser(Long roomId, User user);
}
