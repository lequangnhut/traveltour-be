package com.main.traveltour.restcontroller.staff.tour;

import com.main.traveltour.dto.staff.tour.ToursDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class ToursAPI {

    // Tạo một instance của Logger  để ghi nhận các hoạt động và lỗi.
    private static final Logger logger = LoggerFactory.getLogger(ToursAPI.class);

    @Autowired
    private ToursService toursService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("staff/tour/find-all-tours")
    private ResponseObject findAllTours() {
        List<Tours> items = toursService.findAllByIsActiveIsTrue();
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("staff/tour/find-all-tours")
    private ResponseEntity<Page<Tours>> findAllTours(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) { // Thêm tham số tìm kiếm
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Tours> items = searchTerm == null || searchTerm.isEmpty()
                ? toursService.findAllByIsActiveIsTrue(PageRequest.of(page, size, sort))
                : toursService.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));

        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    //get tour by id rest api
    @GetMapping("staff/tour/find-by-id/{id}")
    private ResponseEntity<Tours> findById(@PathVariable String id) {
        // Tìm tour bằng ID sử dụng service. Nếu không tìm thấy, ném ngoại lệ ResourceNotFoundException.
        Tours tours = toursService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tours not exist with id: " + id));
        // Trả về toursDto với trạng thái HTTP OK.
        return ResponseEntity.ok(tours);
    }


    @PostMapping("staff/tour/create-tour")
    public ResponseEntity<ToursDto> uploadFileAndCreateTour(@RequestPart("toursDto") ToursDto toursDto, @RequestPart("tourImg") MultipartFile tourImg) {
        try {
            String tourId = GenerateNextID.generateNextCode("TR", toursService.maxCodeTourId());
            //Xử lý tải lên file và nhận đường dẫn
            String imagesPath = fileUpload.uploadFile(tourImg);

            // Gán đường dẫn vào đối tượng toursDto
            toursDto.setDateCreated(new Timestamp(System.currentTimeMillis()));

            // Chuyển đổi ToursDto thành entity Tours.
            Tours tours = EntityDtoUtils.convertToEntity(toursDto, Tours.class);
            tours.setId(tourId);
            tours.setTourImg(imagesPath);
            // Lưu tours vào database.
            Tours savedTours = toursService.save(tours);
            // Chuyển đổi tours đã lưu trở lại thành ToursDto.
            ToursDto savedToursDto = EntityDtoUtils.convertToDto(savedTours, ToursDto.class);
            // Trả về toursDto với trạng thái HTTP CREATED.
            return new ResponseEntity<>(savedToursDto, HttpStatus.CREATED);
        } catch (Exception e) {
            // Ghi nhận lỗi vào log và trả về lỗi server.
            logger.error("Error when creating tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //updateStatusAndActive Tours rest api
    @PutMapping("staff/tour/update-tour/{id}")
    public ResponseEntity<Tours> updateTourById(@PathVariable String id, @RequestPart("toursDto") ToursDto toursDto, @RequestPart(required = false) MultipartFile tourImg) {
        try {
            toursDto.setId(id);
            Tours tours = EntityDtoUtils.convertToEntity(toursDto, Tours.class);
            Tours existingTour = toursService.findById(id).orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + id));

            String currentImagePath = existingTour.getTourImg();

            if (tourImg != null) {
                String imagesPath = fileUpload.uploadFile(tourImg);
                tours.setTourImg(imagesPath);
            } else {
                tours.setTourImg(currentImagePath);
            }

            Tours updatedTour = toursService.save(tours);
            return ResponseEntity.ok(updatedTour);
        } catch (Exception e) {
            logger.error("Error when updating tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("staff/tour/deactivate-tour/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTourById(@PathVariable String id) {
        try {
            Tours tours = toursService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tours not exist with id: " + id));
            tours.setIsActive(Boolean.FALSE);
            toursService.save(tours);
            Map<String, Boolean> response = new HashMap<>();
            response.put("delete", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
