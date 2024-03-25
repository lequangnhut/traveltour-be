package com.main.traveltour.dto.chat;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateMessageDto {
    private String senderId;
    private String recipientId;
}
