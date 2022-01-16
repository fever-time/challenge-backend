package shop.fevertime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.fevertime.backend.domain.ChatRoom;
import shop.fevertime.backend.domain.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByIdAndUser(Long roomId, User user);
}
