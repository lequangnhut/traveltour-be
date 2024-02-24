package com.main.traveltour.repository;

import com.main.traveltour.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findAllByOrderByDateCreatedDesc();
}
