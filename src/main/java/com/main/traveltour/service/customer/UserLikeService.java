package com.main.traveltour.service.customer;

import com.main.traveltour.entity.UserLikes;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface UserLikeService {
    void save(UserLikes userLikes);

    List<UserLikes> findFavoritesList(Integer usersId);

    Boolean existsByServiceIdAndUserId(String serviceId, Integer userId);

    void deleteByServiceIdAndUsersId(String serviceId, Integer userId);
}
