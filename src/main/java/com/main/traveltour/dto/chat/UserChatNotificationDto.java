package com.main.traveltour.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChatNotificationDto {
    private Integer senderId;
    private Integer recipientId;
    private String content;
}
