package com.main.traveltour.service.chat;

import com.main.traveltour.entity.ChatRooms;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public ChatRoomRepositoryCustom(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertChatRoom(ChatRooms chatRooms) {
        String sql = "INSERT INTO chat_rooms (chat_id, sender_id, recipient_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, chatRooms.getChatId(), chatRooms.getSenderId(), chatRooms.getRecipientId());
    }
}
