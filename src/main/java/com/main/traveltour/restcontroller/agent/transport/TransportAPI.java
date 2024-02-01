package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.agent.transport.TransportationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.TransportationImageService;
import com.main.traveltour.service.agent.TransportationService;
import com.main.traveltour.service.agent.TransportationTypeService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private TransportationService transportationService;

    @Autowired
    private TransportationTypeService transportationTypeService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private TransportationImageService transportationImageService;

    @GetMapping("/agent/transportation/find-all-transportation/{brandId}")
    private ResponseEntity<Page<Transportations>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String sortDir,
                                                                        @RequestParam(required = false) String searchTerm,
                                                                        @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Transportations> transportationBrands = searchTerm == null || searchTerm.isEmpty()
                ? transportationService.findAllTransports(brandId, PageRequest.of(page, size, sort))
                : transportationService.findAllTransportWithSearch(brandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(transportationBrands, HttpStatus.OK);
    }

    @GetMapping("/agent/transportation/find-all-transportation-type")
    private ResponseObject findAllTransportType() {
        List<TransportationTypes> transportationTypes = transportationTypeService.findAllTransportType();

        if (transportationTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationTypes);
        }
    }

    @GetMapping("/agent/transportation/find-image-by-transportId/{transportId}")
    private ResponseObject findImageByTransportId(@PathVariable String transportId) {
        List<TransportationImage> transportationImages = transportationImageService.findByTransportId(transportId);

        if (transportationImages.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationImages);
        }
    }

    @GetMapping("/agent/transportation/find-by-transport-brandId/{transportBrandId}")
    private ResponseObject findByTransportBrandId(@PathVariable String transportBrandId) {
        TransportationBrands brands = transportationBrandsService.findByTransportBrandId(transportBrandId);

        if (brands == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brands);
        }
    }

    @GetMapping("/agent/transportation/find-by-transport-type-id/{transportTypeId}")
    private ResponseObject findByTransportTypeId(@PathVariable int transportTypeId) {
        TransportationTypes transportationsTypes = transportationTypeService.findByTransportTypeId(transportTypeId);

        if (transportationsTypes == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationsTypes);
        }
    }

    @GetMapping("/agent/transportation/find-by-transportation-id/{transportId}")
    private ResponseObject findByTransportId(@PathVariable String transportId) {
        Transportations transportations = transportationService.findTransportById(transportId);

        if (transportations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportations);
        }
    }

    @GetMapping("/agent/transportation/check-duplicate-license-plate/{licensePlate}")
    private Map<String, Boolean> checkDuplicateLicensePlate(@PathVariable String licensePlate) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = transportationService.findTransportByLicensePlate(licensePlate) != null;

        response.put("exists", exists);
        return response;
    }

    @PostMapping(value = "/agent/transportation/create-transportation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private void createTrans(@RequestPart("transportationsDto") TransportationsDto transportationsDto,
                             @RequestPart("transportTypeImg") List<MultipartFile> transportTypeImg,
                             @RequestPart("transportationImg") MultipartFile transportationImg) throws IOException {
        String transportId = GenerateNextID.generateNextCode("TP", transportationService.findMaxCode());
        String avatarTransport = fileUpload.uploadFile(transportationImg);

        Transportations transportation = EntityDtoUtils.convertToEntity(transportationsDto, Transportations.class);
        transportation.setId(transportId);
        transportation.setIsActive(Boolean.TRUE);
        transportation.setDateCreated(new Timestamp(System.currentTimeMillis()));
        transportation.setTransportationImg(avatarTransport);
        transportationService.save(transportation);

        for (MultipartFile file : transportTypeImg) {
            String imgPath = fileUpload.uploadFile(file);
            TransportationImage image = new TransportationImage();
            image.setTransportationId(transportId);
            image.setImagePath(imgPath);
            transportationImageService.save(image);
        }
    }

    @PutMapping(value = "/agent/transportation/update-transportation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private void updateTrans(@RequestPart("transportationsDto") TransportationsDto transportationsDto,
                             @RequestPart(value = "transportationImg", required = false) MultipartFile transportationImg) throws IOException {
        Transportations transportations = transportationService.findTransportById(transportationsDto.getId());

        if (transportationImg != null) {
            String avatarTransport = fileUpload.uploadFile(transportationImg);
            transportations.setTransportationImg(avatarTransport);
            transportationService.save(transportations);
        } else {
            transportations.setTransportationBrandId(transportationsDto.getTransportationBrandId());
            transportations.setTransportationTypeId(transportationsDto.getTransportationTypeId());
            transportations.setLicensePlate(transportationsDto.getLicensePlate());
            transportations.setAmountSeat(transportationsDto.getAmountSeat());
            transportations.setIsActive(transportationsDto.getIsActive());
            transportationService.save(transportations);
        }
    }

    @GetMapping("/agent/transportation/delete-transportation/{transportId}")
    private void deleteTrans(@PathVariable String transportId) {
        Transportations transportation = transportationService.findTransportById(transportId);
        transportation.setIsActive(Boolean.FALSE);
        transportationService.save(transportation);
    }
}
