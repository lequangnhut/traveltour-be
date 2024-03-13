package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.TransportationSchedulesDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.staff.BookingTourTransportationService;
import com.main.traveltour.service.staff.OrderTransportationService;
import com.main.traveltour.service.staff.TransportationScheduleService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour-transportation/")
public class BookingTourTransportationAPI {

    @Autowired
    private BookingTourTransportationService bookingTourTransportationService;

    @Autowired
    private OrderTransportationService orderTransportationService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @GetMapping("find-all-booking-tour-transportation")
    public ResponseObject getAllBookingTourHotel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportationSchedules> transportationSchedules = bookingTourTransportationService.findTransportationSchedulesByTourDetailId(tourDetailId, orderStatus, searchTerm, PageRequest.of(page, size, sort));

        Page<TransportationSchedulesDto> transportationSchedulesDtos = transportationSchedules.map(transportationSchedule -> EntityDtoUtils.convertToDto(transportationSchedule, TransportationSchedulesDto.class));

        if (transportationSchedulesDtos.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);
        }
    }

    @DeleteMapping("/delete-order-transportation-and-transportation-schedule-by-transportation-schedule-id/{transportationScheduleId}")
    public ResponseObject delete(@PathVariable String transportationScheduleId) {
        try {
            List<OrderTransportations> orderTransportations = orderTransportationService.findAllByTransportationScheduleId(transportationScheduleId);
            for (OrderTransportations orderTransport : orderTransportations) {
                orderTransport.setOrderStatus(2);
                orderTransportationService.update(orderTransport);
            }

            TransportationSchedules transportationSchedule = transportationScheduleService.findById(transportationScheduleId);
            transportationSchedule.setIsStatus(1); // ngưng hoạt động
            transportationScheduleService.update(transportationSchedule);

            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @PutMapping("/restore-order-transportation-and-transportation-schedule-by-transportation-schedule-id")
    public ResponseObject restore(@RequestParam(required = false) String transportationScheduleId) {
        try {
            List<OrderTransportations> orderTransportations = orderTransportationService.findAllByTransportationScheduleId(transportationScheduleId);
            for (OrderTransportations orderTransport : orderTransportations) {
                orderTransport.setOrderStatus(0); // Giả sử 0 là trạng thái khôi phục
                orderTransportationService.update(orderTransport);
            }

            TransportationSchedules transportationSchedule = transportationScheduleService.findById(transportationScheduleId);
            transportationSchedule.setIsStatus(0); // Giả sử 0 là trạng thái khôi phục
            transportationScheduleService.update(transportationSchedule);

            return new ResponseObject("204", "Khôi phục thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Khôi phục thất bại", null);
        }
    }


}
