package com.main.traveltour.restcontroller.staff.tour;

import com.main.traveltour.dto.staff.tour.TourDetailImagesDto;
import com.main.traveltour.dto.staff.tour.TourDetailsDto;
import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDestinations;
import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.TourDestinationService;
import com.main.traveltour.service.staff.TourDetailsImageService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/tour-detail")
public class TourDetailsAPI {

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private TourDetailsImageService tourDetailsImageService;

    @Autowired
    private TourDestinationService tourDestinationService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("/find-all-tourDetail")
    private ResponseObject findAllTourDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer tourDetailStatus) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TourDetails> tourDetailsPage = searchTerm == null || searchTerm.isEmpty()
                ? tourDetailsService.findAllTourDetailStaff(tourDetailStatus, PageRequest.of(page, size, sort))
                : tourDetailsService.findAllTourDetailWithSearchStaff(tourDetailStatus, searchTerm, PageRequest.of(page, size, sort));

        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    @GetMapping("/find-all-tourDetail-select")
    private ResponseObject findAllTourDetailSelect() {
        // Sử dụng phương thức tìm kiếm mới trong service
        List<TourDetails> tourDetailsList = tourDetailsService.findAll();

        List<TourDetailsGetDataDto> tourDetailsDtoList = EntityDtoUtils.convertToDtoList(tourDetailsList, TourDetailsGetDataDto.class);

        if (tourDetailsDtoList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoList);
        }
    }


    @GetMapping("/find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<TourDetails> tourDetails = Optional.ofNullable(tourDetailsService.findById(id));
        TourDetailsGetDataDto tourDetailsDto = EntityDtoUtils.convertOptionalToDto(tourDetails, TourDetailsGetDataDto.class);

        if (tourDetailsDto == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDto);
        }
    }

    @GetMapping("/find-all-join-booking")
    public ResponseObject getAllJoinBooking() {
        List<TourDetails> tourDetailsList = tourDetailsService.getAllJoinBooking();
        List<TourDetailsGetDataDto> tourDetailsDtoList = tourDetailsList.stream()
                .map(tourDetail -> EntityDtoUtils.convertToDto(tourDetail, TourDetailsGetDataDto.class))
                .toList();
        if (tourDetailsDtoList.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoList);
        }
    }

    @GetMapping("/find-tour-destination-by-tour-detail-id/{tourDetailId}")
    public ResponseObject findTourDestinationByTourDetailId(@PathVariable String tourDetailId) {
        List<TourDestinations> tourDestinations = tourDestinationService.findAllByTourDetailId(tourDetailId);

        if (tourDestinations.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDestinations);
        }
    }

    @PostMapping(value = "/create-tourDetail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObject createTourDetail(@RequestPart TourDetailsDto tourDetailsDto, @RequestPart List<MultipartFile> tourDetailImage) {
        try {
            String tourDetailId = GenerateNextID.generateNextCode("TD", tourDetailsService.getMaxCodeTourDetailId());
            TourDetails tourDetail = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            tourDetail.setId(tourDetailId);
            tourDetail.setTourDetailStatus(1);
            tourDetail.setBookedSeat(0);
            tourDetail.setDateCreated(new Timestamp(System.currentTimeMillis()));
            tourDetail = tourDetailsService.save(tourDetail);

            for (MultipartFile file : tourDetailImage) {
                String imgPath = fileUpload.uploadFile(file);
                TourDetailImages image = new TourDetailImages();
                image.setTourDetailId(tourDetailId);
                image.setTourDetailImg(imgPath);
                tourDetailsImageService.save(image);
            }
            return new ResponseObject("200", "Thêm mới thành công", tourDetail);
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

    @PutMapping("/update-tourDetail/{id}")
    public ResponseObject updateTourDetail(@PathVariable String id, @RequestPart TourDetailsDto tourDetailsDto) {
        try {
            Optional<TourDetails> detailsOptional = Optional.ofNullable(tourDetailsService.findById(id));

            if (detailsOptional.isPresent()) {
                TourDetails details = detailsOptional.get();

                if (!details.getFromLocation().equals(tourDetailsDto.getFromLocation()) || !details.getToLocation().equals(tourDetailsDto.getToLocation())) {
                    List<TourDestinations> destinations = tourDestinationService.findAllByTourDetailId(tourDetailsDto.getId());

                    if (!destinations.isEmpty()) {
                        tourDestinationService.deleteAll(tourDetailsDto.getId());
                    }
                }
            }

            tourDetailsDto.setId(id);
            TourDetails tourDetails = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            TourDetails updatedTourDetail = tourDetailsService.save(tourDetails);
            return new ResponseObject("200", "Cập nhật thành công", updatedTourDetail);
        } catch (Exception e) {
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("/delete-tourDetail")
    public ResponseObject deleteTourDetail(@RequestParam String tourDetail,
                                           @RequestParam String tourDetailNotes) {
        try {
            TourDetails tourDetails = tourDetailsService.getById(tourDetail);
            tourDetails.setTourDetailStatus(4);
            tourDetails.setDateDeleted(new Timestamp(System.currentTimeMillis()));
            tourDetails.setTourDetailNotes(tourDetailNotes);
            tourDetailsService.save(tourDetails);
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @PostMapping("/create-tour-destination/{tourDetailId}")
    public ResponseObject createTourDestination(@RequestBody List<String> dataProvince, @PathVariable String tourDetailId) {
        try {
            List<TourDestinations> destinations = tourDestinationService.findAllByTourDetailId(tourDetailId);

            if (destinations.isEmpty()) {
                for (String province : dataProvince) {
                    TourDestinations tourDestinations = new TourDestinations();
                    tourDestinations.setTourDetailId(tourDetailId);
                    tourDestinations.setProvince(province);
                    tourDestinationService.save(tourDestinations);
                }
            } else {
                tourDestinationService.deleteAll(tourDetailId);

                for (String province : dataProvince) {
                    TourDestinations tourDestinations = new TourDestinations();
                    tourDestinations.setTourDetailId(tourDetailId);
                    tourDestinations.setProvince(province);
                    tourDestinationService.save(tourDestinations);
                }
            }
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Thất bại", null);
        }
    }

    @PostMapping(value = "/create-tour-detail-image/{tourDetailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObject createTourDetailImage(@RequestPart List<MultipartFile> tourDetailImage, @PathVariable String tourDetailId) {
        try {
            for (MultipartFile file : tourDetailImage) {
                String imgPath = fileUpload.uploadFile(file);
                TourDetailImages image = new TourDetailImages();
                image.setTourDetailId(tourDetailId);
                image.setTourDetailImg(imgPath);
                tourDetailsImageService.save(image);
            }
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Thất bại", null);
        }
    }

    @PutMapping("/update-tour-detail-image")
    public ResponseObject updateTourDetailImage(@RequestBody List<TourDetailImagesDto> tourDetailImagesDtoList) {
        try {
            for (TourDetailImagesDto tourDetailImagesDto : tourDetailImagesDtoList) {
                String tourDetailId = tourDetailImagesDto.getTourDetailId();

                tourDetailsImageService.delete(tourDetailId);

                for (TourDetailImagesDto dto : tourDetailImagesDtoList) {
                    TourDetailImages newImage = EntityDtoUtils.convertToEntity(dto, TourDetailImages.class);
                    tourDetailsImageService.save(newImage);
                }
            }
            return new ResponseObject("204", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Thất bại", e);
        }
    }
}
