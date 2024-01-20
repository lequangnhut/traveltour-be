package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.ToursDto;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("staff/tour/find-all-tours")
    private ResponseEntity<Page<Tours>> findAllTours(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) { // Thêm tham số tìm kiếm
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Tours> items = searchTerm == null || searchTerm.isEmpty()
                ? toursService.findAll(PageRequest.of(page, size, sort))
                : toursService.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));

        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    //get tour by id rest api
    @GetMapping("staff/tour/find-by-id/{id}")
    private ResponseEntity<Tours> findById(@PathVariable int id) {
        // Tìm tour bằng ID sử dụng service. Nếu không tìm thấy, ném ngoại lệ ResourceNotFoundException.
        Tours tours = toursService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tours not exist with id: " + id));
        // Trả về toursDto với trạng thái HTTP OK.
        return ResponseEntity.ok(tours);
    }


    @PostMapping("staff/tour/create-tour")
    public ResponseEntity<ToursDto> uploadFileAndCreateTour(@RequestPart("toursDto") ToursDto toursDto, @RequestPart("tourImg") MultipartFile tourImg) {

        try {
            //Xử lý tải lên file và nhận đường dẫn
            String imagesPath = fileUpload.uploadFile(tourImg);

            // Gán đường dẫn vào đối tượng toursDto
            toursDto.setDateCreated(new Timestamp(System.currentTimeMillis()));

            // Chuyển đổi ToursDto thành entity Tours.
            Tours tours = EntityDtoUtils.convertToEntity(toursDto, Tours.class);
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


    //update Tours rest api
    @PutMapping("staff/tour/update-tour/{id}")
    public ResponseEntity<Tours> updateTourById(@PathVariable int id, @RequestPart("toursDto") ToursDto toursDto, @RequestPart("tourImg") MultipartFile tourImg) {
        try {
            toursDto.setId(id);
            String imagesPath = fileUpload.uploadFile(tourImg);
            Tours tours = EntityDtoUtils.convertToEntity(toursDto, Tours.class);
            tours.setTourImg(imagesPath);
            Tours updatedTour = toursService.save(tours);
            return ResponseEntity.ok(updatedTour);
        } catch (Exception e) {
            logger.error("Error when updating tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("staff/tour/deactivate-tour/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTourById(@PathVariable int id) {
        try {
            // Tìm tour bằng ID. Nếu không tìm thấy, ném ResourceNotFoundException.
            Tours tours = toursService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tours not exist with id: " + id));
            // Đặt trạng thái của tour thành FALSE (hủy kích hoạt).
            tours.setIsActive(Boolean.FALSE);
            // Lưu thay đổi vào database.
            toursService.save(tours);
            // Tạo và trả về response với thông báo hủy kích hoạt thành công.
            Map<String, Boolean> response = new HashMap<>();
            response.put("delete", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Ghi nhận lỗi vào log và trả về lỗi server.
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
