package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.customer.transport.TransportationSchedulesDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarDetailDto;
import com.main.traveltour.dto.staff.requestcar.RequestCarGetDataDto;
import com.main.traveltour.entity.RequestCar;
import com.main.traveltour.entity.RequestCarDetail;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.staff.RequestCarDetailService;
import com.main.traveltour.service.staff.RequestCarService;
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
@RequestMapping("api/v1/agent/request-car/")
public class RequestCarAGAPI {

    @Autowired
    private RequestCarService requestCarService;

    @Autowired
    private RequestCarDetailService requestCarDetailService;

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
            Page<RequestCarGetDataDto> requestCarGetDataDto = requestCars.map(requestCar -> EntityDtoUtils.convertToDto(requestCar, RequestCarGetDataDto.class));

            return new ResponseObject("200", "Thành công", requestCarGetDataDto);
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

    @GetMapping("find-request-car-submitted")
    private ResponseObject findRequestCarSubmitted(@RequestParam Integer requestCarId,
                                                   @RequestParam String transportBrandId) {
        try {
            RequestCar requestCar = requestCarService.findTransportBrandSubmitted(requestCarId, transportBrandId);

            if (requestCar != null) {
                return new ResponseObject("200", "Thành công", true);
            } else {
                return new ResponseObject("200", "Không tìm thấy dữ liệu", false);
            }
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("find-all-transport-schedule-by-transport-brand-id/{transportBrandId}")
    private ResponseObject findAllTransportByBrandId(@PathVariable String transportBrandId) {
        try {
            List<TransportationSchedules> transportationSchedules = transportationScheduleService.findAllScheduleByBrandIdRequestCar(transportBrandId);
            List<TransportationSchedulesDto> transportationSchedulesDto = EntityDtoUtils.convertToDtoList(transportationSchedules, TransportationSchedulesDto.class);

            return new ResponseObject("200", "Thành công", transportationSchedulesDto);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PostMapping("submit-request-car-to-staff")
    private ResponseObject submitRequestDetail(@RequestBody RequestCarDetailDto requestCarDetailDto) {
        try {
            RequestCarDetail requestCarDetail = EntityDtoUtils.convertToEntity(requestCarDetailDto, RequestCarDetail.class);
            requestCarDetail.setDateCreated(new Timestamp(System.currentTimeMillis()));
            requestCarDetail.setIsAccepted(Boolean.FALSE);
            requestCarDetailService.save(requestCarDetail);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }
}
