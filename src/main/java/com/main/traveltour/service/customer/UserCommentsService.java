package com.main.traveltour.service.customer;

import com.main.traveltour.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;

public interface UserCommentsService {
    Page<UserComments> findCommentsByServiceId(String serviceId, Pageable pageable);
    UserComments findUserCommentsById(Integer id);
    Page<UserComments> findUserCommentsByDateCreate(Timestamp dateCreate, Pageable pageable);
    Page<UserComments> findUserCommentsByStarAndServiceId(Integer start, String serviceId, Pageable pageable);
    void insertUserComments(UserComments userComments);
    void updateUserComments(UserComments userComments);
    void deleteUserComments(Integer commentId);
}
