package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.TourDetailImagesDto;
import com.main.traveltour.dto.staff.TourDetailsDto;
import com.main.traveltour.dto.staff.TourDetailsGetDataDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.entity.TourDetails;
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
    private FileUpload fileUpload;

    @GetMapping("/find-all-tourDetail")
    private ResponseObject findAllTourDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TourDetails> tourDetailsPage = searchTerm == null || searchTerm.isEmpty()
                ? tourDetailsService.findAll(PageRequest.of(page, size, sort))
                : tourDetailsService.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));

        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }


    @GetMapping("/find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<TourDetails> tourDetails = tourDetailsService.findById(id);
        TourDetailsGetDataDto tourDetailsDto = EntityDtoUtils.convertOptionalToDto(tourDetails, TourDetailsGetDataDto.class);

        if (tourDetails.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDto);
        }
    }

    @PostMapping(value = "/create-tourDetail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObject createTourDetail(@RequestPart TourDetailsDto tourDetailsDto, @RequestPart List<MultipartFile> tourDetailImage) {
        try {
            String tourDetailId = GenerateNextID.generateNextCode("TR", tourDetailsService.getMaxCodeTourDetailId());
            TourDetails tourDetail = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            tourDetail.setId(tourDetailId);
            tourDetail.setTourDetailStatus(1);
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
            tourDetailsDto.setId(id);
            TourDetails tourDetails = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            TourDetails updatedTourDetail = tourDetailsService.save(tourDetails);
            return new ResponseObject("200", "Cập nhật thành công", updatedTourDetail);
        } catch (Exception e) {
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("/delete-tourDetail/{id}")
    public ResponseObject deleteTourDetail(@PathVariable String id) {
        try {
            TourDetails tourDetails = tourDetailsService.getById(id);
            tourDetails.setTourDetailStatus(4);
            tourDetailsService.save(tourDetails);
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xó    a thất bại", null);
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
