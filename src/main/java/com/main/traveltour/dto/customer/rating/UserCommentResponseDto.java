package com.main.traveltour.dto.customer.rating;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.main.traveltour.entity.UserComments;
import com.main.traveltour.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

import java.sql.Timestamp;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCommentResponseDto {
    private int id;
    private String serviceId;
    private Integer category;
    private Integer star;
    private String content;
    private Timestamp dateCreated;
    private int usersId;
    private String orderId;
    private Users usersByUserId;
}
