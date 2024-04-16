package com.main.traveltour.dto.customer.rating;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRatingDto {
    private int id;
    private String email;
    private String avatar;
    private String fullName;
}
