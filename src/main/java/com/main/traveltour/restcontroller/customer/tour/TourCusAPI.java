package com.main.traveltour.restcontroller.customer.tour;

import com.main.traveltour.dto.staff.TourDetailsGetDataDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.staff.TourTypesService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class TourCusAPI {

    @Autowired
    private TourTypesService tourTypesService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @GetMapping("customer/tour/find-tour-detail-customer")
    private ResponseObject findAllTourDetail(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        Page<TourDetails> tourDetailsPage = tourDetailsService.findAll(PageRequest.of(page, size));
        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    @GetMapping("customer/tour/find-all-tour-type")
    private ResponseObject findAllTourType() {
        List<TourTypes> tourTypes = tourTypesService.findAll();

        if (tourTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTypes);
        }
    }

    @GetMapping("customer/tour/find-by-tour-detail-id/{tourDetailId}")
    private ResponseObject findByTourDetailId(@PathVariable String tourDetailId) {
        Optional<TourDetails> tourDetails = tourDetailsService.findById(tourDetailId);
        TourDetailsGetDataDto tourDetailsDto = EntityDtoUtils.convertOptionalToDto(tourDetails, TourDetailsGetDataDto.class);

        if (tourDetails.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDto);
        }
    }
}
