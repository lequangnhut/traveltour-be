package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.TransportationBrandsDto;
import com.main.traveltour.entity.TransportationBrands;
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
    private TransportationBrandsService transportationBrandsService;

    @GetMapping("/agent/transport/find-by-agency-id/{agencyId}")
    private TransportationBrands findByUserId(@PathVariable int agencyId) {
        return transportationBrandsService.findByAgencyId(agencyId);
    }

    @PostMapping("/agent/transport/register-transport")
    private void registerTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto, @RequestPart("transportImg") MultipartFile transportImg) throws IOException {
        int agencyId = transportDto.getId();
        String transportImgUrl = fileUpload.uploadFile(transportImg);

        TransportationBrands transport = transportationBrandsService.findByAgencyId(agencyId);
        transport.setTransportationBrandName(transportDto.getTransportationBrandName());
        transport.setTransportationBrandDescription(transportDto.getTransportationBrandDescription());
        transport.setTransportationBrandImg(transportImgUrl);
        transport.setIsAccepted(Boolean.TRUE);
        transportationBrandsService.save(transport);
    }
}
