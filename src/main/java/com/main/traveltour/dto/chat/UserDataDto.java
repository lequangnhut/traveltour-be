package com.main.traveltour.dto.chat;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class UserDataDto {
    private String userId;
    private String role;
}
