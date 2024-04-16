package com.main.traveltour.dto.customer.rating;

import com.main.traveltour.entity.UserComments;
import com.main.traveltour.repository.UserCommentsRepository;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RatingResponseDto {
    private Page<UserCommentResponseDto> userComments;
    private Double roundedAverageRating;
    private Integer numberOfRatings;
}
