package com.main.traveltour.service.customer;

import com.main.traveltour.dto.customer.rating.RatingResponseDto;
import com.main.traveltour.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Optional;

public interface UserCommentsService {
    RatingResponseDto findCommentsByServiceId(String serviceId, Pageable pageable);
    UserComments findUserCommentsById(Integer id);
    Optional<UserComments> findByOrderIdRating(String orderId);
    Page<UserComments> findUserCommentsByDateCreate(Timestamp dateCreate, Pageable pageable);
    Page<UserComments> findUserCommentsByStarAndServiceId(Integer start, String serviceId, Pageable pageable);
    void insertUserComments(UserComments userComments);
    void updateUserComments(UserComments userComments);
    void deleteUserComments(Integer commentId);
}
