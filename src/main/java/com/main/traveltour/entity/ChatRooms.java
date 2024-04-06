package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_rooms", schema = "travel_tour")
public class ChatRooms {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "chat_id")
    private String chatId;

    @Basic
    @Column(name = "sender_id")
    private String senderId;

    @Basic
    @Column(name = "recipient_id")
    private String recipientId;
}
