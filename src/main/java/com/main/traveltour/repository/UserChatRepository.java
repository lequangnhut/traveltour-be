package com.main.traveltour.repository;

import com.main.traveltour.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Integer>{
    Optional<UserChat> findByUserId(String id);

    List<UserChat> findByRoleAndStatus(String role, String status);

    List<UserChat> findAllByUserIdIn(List<String> userIds);
}
