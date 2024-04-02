package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderVisitDetailsGetDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.staff.BookingTourVisitService;
import com.main.traveltour.service.staff.BookingTourVisitService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour-visit/")
public class BookingTourVisitAPI {

    @Autowired
    private BookingTourVisitService bookingTourVisitService;


    @GetMapping("find-all-booking-tour-visit")
    public ResponseObject getAllBookingTourVisit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) Integer orderVisitStatus,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<VisitLocations> visitLocations = bookingTourVisitService.findVisitByTourDetailId(tourDetailId, orderVisitStatus, searchTerm, PageRequest.of(page, size, sort));

        if (visitLocations.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @GetMapping("find-all-booking-tour-visit-by-tour-detail-id-and-visit-id")
    public ResponseObject getAllBookingTourVisit(
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String visitId,
            @RequestParam(required = false) Integer orderVisitStatus) {

        List<OrderVisitDetails> orderVisitDetails = bookingTourVisitService.findOrderVisitDetailByTourDetailIdAndVisitId(tourDetailId, visitId, orderVisitStatus);
        List<OrderVisitDetailsGetDataDto> orderVisitDetailsGetDataDtos = orderVisitDetails.stream()
                .map(orderVisitDetail -> EntityDtoUtils.convertToDto(orderVisitDetail, OrderVisitDetailsGetDataDto.class))
                .toList();
        if (orderVisitDetails.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderVisitDetailsGetDataDtos);
        }
    }

    @DeleteMapping("/delete-booking-tour-visit-by-tour-detail-id-and-visit-id/{tourDetailId}/{visitId}/{orderVisitStatus}")
    public ResponseObject delete(@PathVariable String tourDetailId,
                                 @PathVariable String visitId,
                                 @PathVariable Integer orderVisitStatus) {
        try {
            List<OrderVisits> orderVisitsList = bookingTourVisitService.findOrderVisitByTourDetailIdAndVisitId(tourDetailId, visitId, orderVisitStatus);
            for (OrderVisits orderVisit : orderVisitsList) {
                orderVisit.setOrderStatus(2);
                bookingTourVisitService.update(orderVisit);
            }
            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @PutMapping("/pay-booking-tour-visit-by-tour-detail-id-and-visit-id")
    public ResponseObject restore(@RequestParam(required = false) String tourDetailId,
                                  @RequestParam(required = false) String visitId,
                                  @RequestParam(required = false) Integer orderVisitStatus,
                                  @RequestParam(required = false) Integer payment) {
        try {
            List<OrderVisits> orderVisitsList = bookingTourVisitService.findOrderVisitByTourDetailIdAndVisitId(tourDetailId, visitId, orderVisitStatus);
            for (OrderVisits orderVisit : orderVisitsList) {
                orderVisit.setOrderStatus(1);
                orderVisit.setPaymentMethod(payment);
                bookingTourVisitService.update(orderVisit);
            }
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", e.getMessage());
        }
    }

}
