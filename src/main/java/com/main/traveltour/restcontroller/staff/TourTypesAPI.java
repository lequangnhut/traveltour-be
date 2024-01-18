package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.dto.staff.TourTypesDto;
import com.main.traveltour.service.staff.TourTypesService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TourTypesAPI {

    // Tạo một instance của Logger  để ghi nhận các hoạt động và lỗi.
    private static final Logger logger = LoggerFactory.getLogger(TourTypesAPI.class);

    @Autowired
    private TourTypesService tourTypesService;

    //get tourType by id rest api
    @GetMapping("staff/tourType/find-by-id/{id}")
    private ResponseEntity<TourTypesDto> findById(@PathVariable int id) {
        // Tìm tour bằng ID sử dụng service. Nếu không tìm thấy, ném ngoại lệ ResourceNotFoundException.
        TourTypes tourTypes = tourTypesService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourType not exist with id: " + id));
        // Chuyển đổi tour thành ToursDto.
        TourTypesDto tourTypesDto = EntityDtoUtils.convertToDto(tourTypes, TourTypesDto.class);
        // Trả về toursDto với trạng thái HTTP OK.
        return ResponseEntity.ok(tourTypesDto);
    }

}
