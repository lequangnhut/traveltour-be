package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderHotelsDto;
import com.main.traveltour.dto.staff.TourDetailImagesDto;
import com.main.traveltour.dto.staff.TourDetailsDto;
import com.main.traveltour.dto.staff.TourDetailsGetDataDto;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.OrderHotelsService;
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
@RequestMapping("api/v1/staff/order-hotel")
public class OrderHotelAPI {

    @Autowired
    private OrderHotelsService orderHotelsService;

//    @GetMapping("/find-by-id/{id}")
//    public ResponseObject findById(@PathVariable String id) {
//        Optional<TourDetails> tourDetails = tourDetailsService.findById(id);
//        TourDetailsGetDataDto tourDetailsDto = EntityDtoUtils.convertOptionalToDto(tourDetails, TourDetailsGetDataDto.class);
//
//        if (tourDetails.isEmpty()) {
//            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
//        } else {
//            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDto);
//        }
//    }

    @PostMapping(value = "create-order-hotel")
    public ResponseObject createOrderHotel(@RequestPart OrderHotelsDto orderHotelsDto) {
        try {
            String orderHotelId = GenerateNextID.generateNextCode("OH", orderHotelsService.maxCodeTourId());
            OrderHotels orderHotels = EntityDtoUtils.convertToEntity(orderHotelsDto, OrderHotels.class);
            orderHotels.setId(orderHotelId);
            orderHotelsService.save(orderHotels);

            return new ResponseObject("200", "Thêm mới thành công", orderHotels);
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

}
