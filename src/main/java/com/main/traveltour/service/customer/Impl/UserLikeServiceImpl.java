package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.UserLikes;
import com.main.traveltour.repository.UserLikesRepository;
import com.main.traveltour.service.customer.UserLikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLikeServiceImpl implements UserLikeService {

    @Autowired
    private UserLikesRepository userLikesRepository;

    @Override
    public void save(UserLikes userLikes) {
        userLikesRepository.save(userLikes);
    }

    @Override
    public List<UserLikes> findFavoritesList(Integer usersId) {
        return userLikesRepository.findAllByUsersId(usersId);
    }

    @Override
    public Boolean existsByServiceIdAndUserId(String serviceId, Integer usersId) {
        return userLikesRepository.existsByServiceIdAndUsersId(serviceId, usersId);    }

    @Override
    @Transactional
    public void deleteByServiceIdAndUsersId(String serviceId, Integer userId) {
        userLikesRepository.deleteByServiceIdAndUsersId(serviceId, userId);
    }
}
