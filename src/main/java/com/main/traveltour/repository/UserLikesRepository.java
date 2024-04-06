package com.main.traveltour.repository;

import com.main.traveltour.entity.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserLikesRepository extends JpaRepository<UserLikes, Integer> {
    List<UserLikes> findAllByUsersId(Integer usersId);

    boolean existsByServiceIdAndUsersId(String serviceId, Integer usersId);

    void deleteByServiceIdAndUsersId(String serviceId, Integer usersId);
}
