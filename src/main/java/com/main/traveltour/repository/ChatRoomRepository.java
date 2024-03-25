package com.main.traveltour.repository;

import com.main.traveltour.entity.ChatMessage;
import com.main.traveltour.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRooms, Integer> {
    Optional<ChatRooms> findBySenderIdAndRecipientId(String senderId, String recipientId);
    Optional<ChatRooms> findFirstBySenderIdAndRecipientId(String senderId, String recipientId);
    List<ChatRooms> findByRecipientId(String recipientId);
    List<ChatRooms> findBySenderId(String senderId);
}
