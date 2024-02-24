package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Notifications;
import com.main.traveltour.repository.NotificationsRepository;
import com.main.traveltour.service.admin.NotificationsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsServiceImplAD implements NotificationsServiceAD {

    @Autowired
    private NotificationsRepository notificationsRepository;
    @Override
    public List<Notifications> findAll() {
        return notificationsRepository.findAllByOrderByDateCreatedDesc();
    }

    @Override
    public Notifications findById(int id) {
        return notificationsRepository.getReferenceById(id);
    }

    @Override
    public void delete(Notifications notifications) {
    notificationsRepository.delete(notifications);
    }
    @Override
    public void save(Notifications notifications) {
        notificationsRepository.save(notifications);
    }
}
