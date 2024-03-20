package com.main.traveltour.dto.chat;

import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserChatResponseDto {
    private Integer id;
    private String userId;
    private String fullName;
    private String status;
    private String avatar;
    private Timestamp lastUpdated;
    private String role;
    private Integer countMessageUnread;
    private Boolean statusMessage;
    private String newMessage;
    private Timestamp timeMessage;
}
