package com.main.traveltour.restcontroller.staff.requestcar;

import com.main.traveltour.dto.staff.requestcar.RequestCarDetailGetDataDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarGetDataDto;
import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.entity.RequestCar;
import com.main.traveltour.entity.RequestCarDetail;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.RequestCarDetailService;
import com.main.traveltour.service.staff.RequestCarService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/staff/request-car/")
public class RequestCarSTFAPI {

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private RequestCarDetailService requestCarDetailService;

    @Autowired
    private RequestCarService requestCarService;

    @GetMapping("find-all-request-car")
    private ResponseObject findAllRequestCar(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Page<RequestCar> requestCars = requestCarService.findAllRequestCarPage(PageRequest.of(page, size, sort));
            Page<RequestCarGetDataDto> requestCarGetDataDto = requestCars.map(requestCar -> EntityDtoUtils.convertToDto(requestCar, RequestCarGetDataDto.class));

            return new ResponseObject("200", "Thành công", requestCarGetDataDto);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-all-request-car-detail")
    private ResponseObject findAllRequestCarDetail(@RequestParam Integer requestCarId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Page<RequestCarDetail> requestCarDetails = requestCarDetailService.findAllRequestCarDetailPage(requestCarId, PageRequest.of(page, size, sort));
            Page<RequestCarDetailGetDataDto> requestCarGetDataDto = requestCarDetails.map(requestCarDetail -> EntityDtoUtils.convertToDto(requestCarDetail, RequestCarDetailGetDataDto.class));

            return new ResponseObject("200", "Thành công", requestCarGetDataDto);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-all-tour-detail-use-request-car")
    private ResponseObject findAllTourDetailUseRequestCar() {
        try {
            List<TourDetails> tourDetails = tourDetailsService.findAllTourDetailUseRequestCar();
            List<TourDetailsGetDataDto> tourDetailsGetDataDto = EntityDtoUtils.convertToDtoList(tourDetails, TourDetailsGetDataDto.class);

            return new ResponseObject("200", "Thành công", tourDetailsGetDataDto);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-request-car-by-id/{requestCarId}")
    private ResponseObject findRequestCarById(@PathVariable Integer requestCarId) {
        try {
            Optional<RequestCar> requestCarOptional = requestCarService.findRequestCarById(requestCarId);
            RequestCar requestCar = requestCarOptional.orElse(null);

            return new ResponseObject("200", "Thành công", requestCar);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PostMapping("create-request-car")
    private ResponseObject createRequestCar(@RequestBody RequestCarDto requestCarDto) {
        try {
            RequestCar requestCar = EntityDtoUtils.convertToEntity(requestCarDto, RequestCar.class);
            requestCar.setDateCreated(new Timestamp(System.currentTimeMillis()));
            requestCar.setIsAccepted(Boolean.FALSE);
            requestCarService.save(requestCar);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PutMapping("update-request-car")
    private ResponseObject updateRequestCar(@RequestBody RequestCarDto requestCarDto) {
        try {
            RequestCar requestCar = EntityDtoUtils.convertToEntity(requestCarDto, RequestCar.class);
            requestCar.setId(requestCarDto.getId());
            requestCarService.save(requestCar);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }
}
