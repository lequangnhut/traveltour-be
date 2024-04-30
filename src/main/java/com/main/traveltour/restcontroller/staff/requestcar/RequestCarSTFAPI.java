package com.main.traveltour.restcontroller.staff.requestcar;

import com.main.traveltour.dto.staff.OrderTransportationsDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarDetailGetDataDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarGetDataDto;
import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.staff.OrderTransportationService;
import com.main.traveltour.service.staff.RequestCarDetailService;
import com.main.traveltour.service.staff.RequestCarService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("api/v1/staff/request-car/")
public class RequestCarSTFAPI {

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private RequestCarDetailService requestCarDetailService;

    @Autowired
    private RequestCarService requestCarService;

    @Autowired
    private OrderTransportationService orderTransportationService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @GetMapping("find-all-request-car")
    private ResponseObject findAllRequestCar(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<RequestCar> requestCars = requestCarService.findAllRequestCarPage(PageRequest.of(page, size, sort));
            Page<RequestCarGetDataDto> requestCarGetDataDto = requestCars.map(
                    requestCar -> EntityDtoUtils.convertToDto(requestCar, RequestCarGetDataDto.class));

            return new ResponseObject("200", "Thành công", requestCarGetDataDto);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-all-request-car-filters")
    private ResponseObject findAllRequestCarFilters(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "desc") String sortDir,
                                                    @RequestParam(required = false) List<Integer> mediaTypeList,
                                                    @RequestParam(required = false) List<String> listOfVehicleManufacturers,
                                                    @RequestParam(required = false) List<Boolean> seatTypeList,
                                                    @RequestParam(required = false) String fromLocation,
                                                    @RequestParam(required = false) String toLocation) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<RequestCar> requestCars = requestCarService.findAllRequestCarsFilters(fromLocation, toLocation, mediaTypeList, listOfVehicleManufacturers,seatTypeList, PageRequest.of(page, size, sort));
            Page<RequestCarGetDataDto> requestCarGetDataDto = requestCars.map(
                    requestCar -> EntityDtoUtils.convertToDto(requestCar, RequestCarGetDataDto.class));

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
                                                   @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<RequestCarDetail> requestCarDetails = requestCarDetailService.findAllRequestCarDetailPage(requestCarId, PageRequest.of(page, size, sort));
            Page<RequestCarDetailGetDataDto> requestCarGetDataDto = requestCarDetails.map(
                    requestCarDetail -> EntityDtoUtils.convertToDto(requestCarDetail, RequestCarDetailGetDataDto.class));

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

    @GetMapping("check-exits-tour-detail")
    private ResponseObject checkExitsTourDetail(@RequestParam String tourDetailId) {
        try {
            List<RequestCar> requestCars = requestCarService.checkExitsTourDetail(tourDetailId);
            return new ResponseObject("200", "Thành công", requestCars.size());
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-car-submitted")
    private ResponseObject findCarSubmitted(@RequestParam Integer requestCarId, @RequestParam String transportationScheduleId) {
        try {
            RequestCarDetail requestCarDetail = requestCarDetailService.findCarSubmitted(requestCarId, transportationScheduleId);

            if (requestCarDetail != null) {
                return new ResponseObject("200", "Thành công", true);
            } else {
                return new ResponseObject("200", "Không tìm thấy dữ liệu", false);
            }
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-request-car-detail-submitted")
    private ResponseObject findRequestCarDetailSubmitted(@RequestParam String transportationScheduleId) {
        try {
            RequestCarDetail requestCarDetail = requestCarDetailService.findRequestCarDetailSubmitted(transportationScheduleId);

            if (requestCarDetail != null) {
                return new ResponseObject("200", "Thành công", true);
            } else {
                return new ResponseObject("200", "Không tìm thấy dữ liệu", false);
            }
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
            requestCar.setIsActive(Boolean.TRUE);
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

    @PostMapping("accept-request-car")
    private ResponseObject acceptRequestCar(@RequestPart String requestCarDetailId,
                                            @RequestPart String requestCarId,
                                            @RequestPart OrderTransportationsDto orderTransportationsDto) {
        try {
            Optional<RequestCarDetail> requestCarDetailOptional = requestCarDetailService.findRequestCarDetailById(Integer.parseInt(requestCarDetailId));

            // update tất cả dữ liệu của nhà xe nộp yêu cầu thành 3: xe không được duyệt
            requestCarDetailService.updateAll(requestCarId);

            requestCarDetailOptional.ifPresent(requestCarDetail -> {
                // cập nhật lại trạng thái request car detail
                requestCarDetail.setIsAccepted(1); // duyệt xe
                requestCarDetail.getRequestCarRequireCarById().setIsAccepted(Boolean.TRUE);
                requestCarDetail.getRequestCarRequireCarById().setDateAccepted(new Timestamp(System.currentTimeMillis()));
                requestCarDetailService.save(requestCarDetail);

                // Thêm vào order transport để thêm xe đã duyệt vào tour
                String tourDetailId = requestCarDetail.getRequestCarRequireCarById().getTourDetailId();

                TransportationSchedules schedules = requestCarDetail.getTransportationSchedulesByTransportationScheduleId();
                schedules.setIsStatus(6); // 6 Đã được duyệt
                transportationScheduleService.save(schedules);

                OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
                Users users = tourDetails.getUsersByGuideId();

                List<TourDetails> tourDetailsList = new ArrayList<>();
                tourDetailsList.add(tourDetails);

                orderTransportations.setTourDetails(tourDetailsList);
                orderTransportations.setTransportationScheduleId(schedules.getId());
                orderTransportations.setCustomerEmail(users.getEmail());
                orderTransportations.setCustomerName(users.getFullName());
                orderTransportations.setCustomerCitizenCard(users.getCitizenCard());
                orderTransportations.setCustomerPhone(users.getPhone());
                orderTransportations.setAmountTicket(requestCarDetail.getRequestCarRequireCarById().getAmountCustomer());
                orderTransportations.setOrderTotal(schedules.getUnitPrice());

                orderTransportationService.save(orderTransportations);
            });
            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }
}
