package com.main.traveltour.repository;

import com.main.traveltour.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String>{
    List<ChatMessage> findByChatId(String chatId);
    Integer countByChatIdAndRecipientIdAndStatusIs(String chatId, String recipient, boolean isRead);
    Integer countBySenderIdAndRecipientIdIs(String senderId, String recipient);
    List<ChatMessage> findByChatIdOrderByTimestampDesc(String chatId);
    ChatMessage findFirstByChatIdOrderByTimestampDesc(String chatId);
    List<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipient);
    List<ChatMessage> findBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, Boolean status);
    ChatMessage findByChatIdAndRecipientId(String chatId, String recipient);

}
