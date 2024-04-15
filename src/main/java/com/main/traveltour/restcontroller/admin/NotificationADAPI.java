package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.NotificationsDtoAD;
import com.main.traveltour.entity.Notifications;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.admin.NotificationsServiceAD;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class NotificationADAPI {

    @Autowired
    private NotificationsServiceAD notificationsServiceAD;

    @GetMapping("/notification-register/findAll")
    private ResponseObject findAll() {
        List<Notifications> notifications = notificationsServiceAD.findAll();
        List<NotificationsDtoAD> notificationsDto = EntityDtoUtils.convertToDtoList(notifications, NotificationsDtoAD.class);

        if (!notificationsDto.isEmpty()) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", notificationsDto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @DeleteMapping("/notification-register/deleteNoted/{id}")
    private void deleteIsSeen(@PathVariable int id) {
        Notifications note = notificationsServiceAD.findById(id);
        notificationsServiceAD.delete(note);
    }

    @PutMapping("/notification-register/updateIsSeen/{id}")
    private void updateIsSeen(@PathVariable int id) {
        Notifications note = notificationsServiceAD.findById(id);
        note.setIsSeen(true);
        notificationsServiceAD.save(note);
    }
}
