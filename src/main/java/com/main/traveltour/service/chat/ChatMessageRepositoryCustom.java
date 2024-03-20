package com.main.traveltour.service.chat;

import com.main.traveltour.entity.ChatMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    public ChatMessageRepositoryCustom(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertChatMessage(ChatMessage chatMessage) {
        String sql = "INSERT INTO chat_message (chat_id, sender_id, recipient_id, content, timestamp, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, chatMessage.getChatId(), chatMessage.getSenderId(), chatMessage.getRecipientId(), chatMessage.getContent(), chatMessage.getTimestamp(), false);
    }

    void updateAllRecipientMessagesStatusToFalse(String chatId, String recipientId) {
        String sql = "UPDATE chat_message SET status = ? WHERE chat_id = ? AND recipient_id = ?";
        jdbcTemplate.update(sql, true, chatId, recipientId);
    }

}
