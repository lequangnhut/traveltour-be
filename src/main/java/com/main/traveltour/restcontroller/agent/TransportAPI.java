package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.transport.TransportDto;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private AgenciesService agenciesService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private UsersService usersService;

    @PostMapping("agent/transport/register-transport")
    private void registerAgentTrans(@RequestPart TransportDto transportDto, @RequestPart("transportationBrandImg") MultipartFile transportationBrandImg, @RequestPart("imgDocument") MultipartFile imgDocument) throws IOException {
        String transportImg = fileUpload.uploadFile(transportationBrandImg);
        String imgDoc = fileUpload.uploadFile(imgDocument);


    }
}
