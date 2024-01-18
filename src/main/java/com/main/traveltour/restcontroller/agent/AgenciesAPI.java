package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.business.AgenciesDto;
import com.main.traveltour.entity.Agencies;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgenciesAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private AgenciesService agenciesService;

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

        agenciesService.save(agencies);
    }
}
