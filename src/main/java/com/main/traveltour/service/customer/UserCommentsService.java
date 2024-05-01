package com.main.traveltour.service.customer;

import com.main.traveltour.dto.customer.rating.RatingResponseDto;
import com.main.traveltour.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserCommentsService {
    RatingResponseDto findCommentsByServiceId(String serviceId, Pageable pageable);
    UserComments findUserCommentsById(Integer id);
    Optional<UserComments> findByOrderIdRating(String orderId);
    Double findScoreRatingByRoomTypeId(String serviceId);
    Page<UserComments> findUserCommentsByStarAndServiceId(Integer start, String serviceId, Pageable pageable);
    void insertUserComments(UserComments userComments);
    void updateUserComments(UserComments userComments);
    void deleteUserComments(Integer commentId);
    List<UserComments> findAllUsersComments();
    Integer findCountRatingByRoomTypeId(String id);
}
