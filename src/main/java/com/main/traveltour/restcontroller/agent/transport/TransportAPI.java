package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.admin.type.TransportUtilitiesDto;
import com.main.traveltour.dto.agent.transport.TransportGetDataDto;
import com.main.traveltour.dto.agent.transport.TransportationImageDto;
import com.main.traveltour.dto.agent.transport.TransportationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.TransportUtilityServiceAD;
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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
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

    @Autowired
    private TransportUtilityServiceAD transportUtilityServiceAD;

    @GetMapping("/agent/transportation/find-all-transportation/{brandId}")
    private ResponseEntity<Page<TransportGetDataDto>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                                            @RequestParam(defaultValue = "desc") String sortDir,
                                                                            @RequestParam(required = false) String searchTerm,
                                                                            @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Transportations> transportationsPage = searchTerm == null || searchTerm.isEmpty()
                ? transportationService.findAllTransports(brandId, PageRequest.of(page, size, sort))
                : transportationService.findAllTransportWithSearch(brandId, searchTerm, PageRequest.of(page, size, sort));

        Page<TransportGetDataDto> transportGetDataDto = transportationsPage.map(brands -> EntityDtoUtils.convertToDto(brands, TransportGetDataDto.class));
        return new ResponseEntity<>(transportGetDataDto, HttpStatus.OK);
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

    @GetMapping("/agent/transportation/find-all-transportation-utilities")
    private ResponseObject findAllTransportUtilities() {
        List<TransportUtilities> transportUtilities = transportUtilityServiceAD.findAllTransUtilityAgent();
        List<TransportUtilitiesDto> transportUtilitiesDto = EntityDtoUtils.convertToDtoList(transportUtilities, TransportUtilitiesDto.class);

        if (transportUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportUtilitiesDto);
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

    @GetMapping("/agent/transportation/find-by-transportation-utilities-by-id/{transportUtilityId}")
    private ResponseObject findByTransportUtilityId(@PathVariable int transportUtilityId) {
        TransportUtilities utilities = transportUtilityServiceAD.findTransUtilityADById(transportUtilityId);

        if (utilities == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", utilities);
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
        Map<String, Object> response = new HashMap<>();

        Optional<Transportations> transportations = transportationService.findTransportById(transportId);
        TransportGetDataDto transportGetDataDto = EntityDtoUtils.convertOptionalToDto(transportations, TransportGetDataDto.class);
        List<TransportUtilities> transportUtilities = transportations.get().getTransportUtilities();

        response.put("transportGetDataDto", transportGetDataDto);
        response.put("transportUtilities", transportUtilities);

        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/agent/transportation/check-duplicate-license-plate/{licensePlate}")
    private Map<String, Boolean> checkDuplicateLicensePlate(@PathVariable String licensePlate) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = transportationService.findTransportByLicensePlate(licensePlate) != null;

        response.put("exists", exists);
        return response;
    }

    @PostMapping(value = "/agent/transportation/create-transportation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseObject createTrans(@RequestPart("transportationsDto") TransportationsDto transportationsDto,
                                       @RequestPart("transportTypeImg") List<MultipartFile> transportTypeImg,
                                       @RequestPart("transportationImg") MultipartFile transportationImg,
                                       @RequestPart(required = false) List<Integer> selectedUtilities) {
        try {
            List<TransportUtilities> utilities = selectedUtilities.stream().map(transportUtilityServiceAD::findTransUtilityADById).toList();
            String transportId = GenerateNextID.generateNextCode("TP", transportationService.findMaxCode());
            String avatarTransport = fileUpload.uploadFile(transportationImg);

            Transportations transportation = EntityDtoUtils.convertToEntity(transportationsDto, Transportations.class);
            transportation.setId(transportId);
            transportation.setIsActive(Boolean.TRUE);
            transportation.setDateCreated(new Timestamp(System.currentTimeMillis()));
            transportation.setTransportUtilities(utilities);
            transportation.setTransportationImg(avatarTransport);
            transportationService.save(transportation);

            for (MultipartFile file : transportTypeImg) {
                String imgPath = fileUpload.uploadFile(file);
                TransportationImage image = new TransportationImage();
                image.setTransportationId(transportId);
                image.setImagePath(imgPath);
                transportationImageService.save(image);
            }
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    @PutMapping(value = "/agent/transportation/update-transportation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseObject updateTrans(@RequestPart("transportationsDto") TransportationsDto transportationsDto,
                                       @RequestPart(value = "transportationImg", required = false) MultipartFile transportationImg,
                                       @RequestPart(required = false) List<Integer> selectedUtilities) {
        try {
            List<TransportUtilities> utilities = selectedUtilities.stream().map(transportUtilityServiceAD::findTransUtilityADById).toList();
            Optional<Transportations> transportationsOptional = transportationService.findTransportById(transportationsDto.getId());

            if (transportationsOptional.isPresent()) {
                Transportations transportations = transportationsOptional.get();

                if (transportationImg != null) {
                    String avatarTransport = fileUpload.uploadFile(transportationImg);
                    transportations.setTransportationImg(avatarTransport);
                    transportationService.save(transportations);
                } else {
                    Transportations transportation = EntityDtoUtils.convertToEntity(transportationsDto, Transportations.class);
                    transportation.setId(transportationsDto.getId());
                    transportation.setTransportationImg(transportations.getTransportationImg());
                    transportation.setTransportUtilities(utilities);
                    transportationService.save(transportation);
                }
            }
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    @GetMapping("/agent/transportation/delete-transportation/{transportId}")
    private void deleteTrans(@PathVariable String transportId) {
        Optional<Transportations> transportationsOptional = transportationService.findTransportById(transportId);

        if (transportationsOptional.isPresent()) {
            Transportations transportation = transportationsOptional.get();
            transportation.setIsActive(Boolean.FALSE);
            transportationService.save(transportation);
        }
    }

    @PostMapping(value = "/agent/transportation/create-transport-detail-image/{transportId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObject createTransportImage(@RequestPart List<MultipartFile> transportImage, @PathVariable String transportId) {
        try {
            for (MultipartFile file : transportImage) {
                String imgPath = fileUpload.uploadFile(file);
                TransportationImage image = new TransportationImage();
                image.setTransportationId(transportId);
                image.setImagePath(imgPath);
                transportationImageService.save(image);
            }
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Thất bại", null);
        }
    }

    @PutMapping("/agent/transportation/update-transport-detail-image")
    public ResponseObject updateTransportImage(@RequestBody List<TransportationImageDto> transportationImageDto) {
        try {
            for (TransportationImageDto transportationImage : transportationImageDto) {
                String transportationId = transportationImage.getTransportationId();

                transportationImageService.delete(transportationId);

                for (TransportationImageDto dto : transportationImageDto) {
                    TransportationImage newImage = EntityDtoUtils.convertToEntity(dto, TransportationImage.class);
                    transportationImageService.save(newImage);
                }
            }
            return new ResponseObject("204", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Thất bại", e);
        }
    }
}
