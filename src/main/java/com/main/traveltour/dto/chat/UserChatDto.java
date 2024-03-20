package com.main.traveltour.dto.chat;

import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserChatDto {
    private Integer id;
    private String userId;
    private String fullName;
    private String status;
    private String avatar;
    private Timestamp lastUpdated;
    private String role;
}
