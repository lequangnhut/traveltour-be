package com.main.traveltour.dto.comment;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserCommentDto {
    private Integer id;
    private String serviceId;
    private Integer star;
    private String content;
    private int usersId;
    private String orderId;
}
