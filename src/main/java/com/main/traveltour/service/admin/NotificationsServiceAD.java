package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Notifications;

import java.util.List;

public interface NotificationsServiceAD {
    List<Notifications> findAll();

    Notifications findById(int id);

    void delete(Notifications notifications);

    void save(Notifications notifications);
}
