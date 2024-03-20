package com.main.traveltour.repository;

import com.main.traveltour.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String>{
    List<ChatMessage> findByChatId(String chatId);

    Integer countByChatIdAndRecipientIdAndStatusIs(String chatId, String recipient, boolean isRead);
    List<ChatMessage> findByChatIdOrderByTimestampDesc(String chatId);

}
