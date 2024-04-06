package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.agent.transport.TransportationBrandsDto;
import com.main.traveltour.entity.ResponseObject;
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

    @GetMapping("/agent/transport-brand/find-by-transport-brand-id/{brandId}")
    private ResponseObject findTransportBrandById(@PathVariable String brandId) {
        TransportationBrands transportationBrands = transportationBrandsService.findByTransportBrandId(brandId);

        if (transportationBrands == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationBrands);
        }
    }

    @PostMapping("/agent/transport-brand/register-transport")
    private void registerTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto,
                                   @RequestPart("transportImg") MultipartFile transportImg) throws IOException {
        int agenciesId = transportDto.getAgenciesId();
        String transportImgUrl = fileUpload.uploadFile(transportImg);

        TransportationBrands transport = transportationBrandsService.findByAgencyId(agenciesId);
        transport.setTransportationBrandName(transportDto.getTransportationBrandName());
        transport.setTransportationBrandPolicy(transportDto.getTransportationBrandPolicy());
        transport.setTransportationBrandAddress(transportDto.getTransportationBrandAddress());
        transport.setTransportationBrandImg(transportImgUrl);
        transport.setIsAccepted(Boolean.TRUE);
        transportationBrandsService.save(transport);
    }

    @PostMapping("/agent/transport-brand/create-transport")
    private void createTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto,
                                 @RequestPart("transportImg") MultipartFile transportImg) throws IOException {
        int agenciesId = transportDto.getAgenciesId();
        String transportBrandId = GenerateNextID.generateNextCode("TRP", transportationBrandsService.findMaxCode());
        String transportImgUrl = fileUpload.uploadFile(transportImg);

        TransportationBrands transport = EntityDtoUtils.convertToEntity(transportDto, TransportationBrands.class);
        transport.setId(transportBrandId);
        transport.setAgenciesId(agenciesId);
        transport.setTransportationBrandImg(transportImgUrl);
        transport.setIsAccepted(Boolean.TRUE);
        transport.setIsActive(Boolean.TRUE);
        transport.setDateCreated(new Timestamp(System.currentTimeMillis()));
        transportationBrandsService.save(transport);
    }

    @PutMapping("/agent/transport-brand/update-transport")
    private void updateTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto,
                                 @RequestPart(value = "transportImg", required = false) MultipartFile transportImg) throws IOException {
        String transportBrandId = transportDto.getId();
        TransportationBrands transport = transportationBrandsService.findByTransportBrandId(transportBrandId);

        if (transportImg != null) {
            String transportImgUrl = fileUpload.uploadFile(transportImg);
            transport.setTransportationBrandName(transportDto.getTransportationBrandName());
            transport.setTransportationBrandPolicy(transportDto.getTransportationBrandPolicy());
            transport.setTransportationBrandAddress(transportDto.getTransportationBrandAddress());
            transport.setTransportationBrandImg(transportImgUrl);
            transportationBrandsService.save(transport);
        } else {
            transport.setTransportationBrandName(transportDto.getTransportationBrandName());
            transport.setTransportationBrandPolicy(transportDto.getTransportationBrandPolicy());
            transport.setTransportationBrandAddress(transportDto.getTransportationBrandAddress());
            transportationBrandsService.save(transport);
        }
    }

    @GetMapping("/agent/transport-brand/delete-transport/{transportBrandId}")
    private void deleteTrans(@PathVariable String transportBrandId) {
        TransportationBrands transportationBrands = transportationBrandsService.findByTransportBrandId(transportBrandId);
        transportationBrands.setIsActive(Boolean.FALSE);
        transportationBrandsService.save(transportationBrands);
    }
}
