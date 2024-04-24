package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "chat_message", schema = "travel_tour")

public class ChatMessage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "chat_id")
    private String chatId;

    @Basic
    @Column(name = "sender_id")
    private String senderId;

    @Basic
    @Column(name = "recipient_id")
    private String recipientId;

    @Basic
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Basic
    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Basic
    @Column(name = "status")
    private Boolean status;
}

