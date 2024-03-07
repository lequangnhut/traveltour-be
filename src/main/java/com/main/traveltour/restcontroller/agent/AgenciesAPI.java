package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.Notifications;
import com.main.traveltour.service.admin.NotificationsServiceAD;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgenciesAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private AgenciesService agenciesService;

    @Autowired
    private NotificationsServiceAD notificationsServiceAD;

    @GetMapping("/agent/agencies/find-by-user-id/{userId}")
    private Agencies findAllAccountStaff(@PathVariable int userId) {
        return agenciesService.findByUserId(userId);
    }

    @PutMapping("/agent/agencies/register-business")
    private void registerAgencies(@RequestPart("agenciesDto") AgenciesDto agenciesDto, @RequestPart("imgDocument") MultipartFile imgDocument) throws IOException {
        String imgDoc = fileUpload.uploadFile(imgDocument);

        Agencies agencies = EntityDtoUtils.convertToEntity(agenciesDto, Agencies.class);
        agencies.setImgDocument(imgDoc);
        agencies.setIsAccepted(1);
        agencies.setDateCreated(new Timestamp(System.currentTimeMillis()));

        Agencies agency = agenciesService.save(agencies); // Lưu và nhận ID
        int agencyId = agency.getId(); // Lấy ID của đối tượng đã được lưu

        createNotification(agencyId, "(D.Nghiệp) " + agencies.getNameAgency() + " nộp hồ sơ đăng ký.");
    }

    @GetMapping("/agent/agencies/check-duplicate-phone/{phone}")
    private Map<String, Boolean> checkDuplicatePhone(@PathVariable String phone) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = agenciesService.findByPhone(phone) != null;

        response.put("exists", exists);
        return response;
    }

    @GetMapping("/agent/agencies/check-duplicate-taxId/{taxId}")
    private Map<String, Boolean> checkDuplicateTaxId(@PathVariable String taxId) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = agenciesService.findByTaxId(taxId) != null;

        response.put("exists", exists);
        return response;
    }

    private void createNotification(int agenciesId, String message) {
        Notifications notification = new Notifications();
        notification.setAgenciesId(agenciesId);
        notification.setStatusMessage(message);
        notification.setIsSeen(false);
        notification.setDateCreated(new Timestamp(System.currentTimeMillis()));
        notificationsServiceAD.save(notification);
    }
}
