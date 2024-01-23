package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.TransportationBrandsDto;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportBrandAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @GetMapping("/agent/transport-brand/find-all-transport-brand/{agencyId}")
    private List<TransportationBrands> findAllTransportBrand(@PathVariable int agencyId) {
        return transportationBrandsService.findAllByAgencyId(agencyId);
    }

    @PostMapping("/agent/transport-brand/register-transport")
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

    @PostMapping("/agent/transport-brand/create-transport")
    private void createTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto, @RequestPart("transportImg") MultipartFile transportImg) throws IOException {
        int agencyId = transportDto.getId();
        String transportBrandId = GenerateNextID.generateNextCode("TRP", transportationBrandsService.findMaxCode());
        String transportImgUrl = fileUpload.uploadFile(transportImg);

        TransportationBrands transport = EntityDtoUtils.convertToEntity(transportDto, TransportationBrands.class);
        transport.setId(transportBrandId);
        transport.setAgenciesId(agencyId);
        transport.setTransportationBrandImg(transportImgUrl);
        transport.setIsAccepted(Boolean.TRUE);
        transport.setIsActive(Boolean.TRUE);
        transport.setDateCreated(new Timestamp(System.currentTimeMillis()));
        transportationBrandsService.save(transport);
    }
}
