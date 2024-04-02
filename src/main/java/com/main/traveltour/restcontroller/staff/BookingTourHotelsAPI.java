package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderHotelDetailsGetDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.staff.BookingTourHotelService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour-hotel/")
public class BookingTourHotelsAPI {

    @Autowired
    private BookingTourHotelService bookingTourHotelService;


    @GetMapping("find-all-booking-tour-hotel")
    public ResponseObject getAllBookingTourHotel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) Integer orderHotelStatus,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Hotels> orderHotels = bookingTourHotelService.findHotelByTourDetailId(tourDetailId, orderHotelStatus, searchTerm, PageRequest.of(page, size, sort));

        if (orderHotels.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderHotels);
        }
    }

    @GetMapping("find-all-booking-tour-hotel-by-tour-detail-id-and-hotel-id")
    public ResponseObject getAllBookingTourHotel(
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String hotelId) {

        List<OrderHotelDetails> orderHotelDetails = bookingTourHotelService.findOrderHotelDetailByTourDetailIdAndHotelId(tourDetailId, hotelId);
        List<OrderHotelDetailsGetDataDto> orderHotelDetailsGetDataDtos = orderHotelDetails.stream()
                .map(orderHotelDetail -> EntityDtoUtils.convertToDto(orderHotelDetail, OrderHotelDetailsGetDataDto.class))
                .toList();
        if (orderHotelDetails.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderHotelDetailsGetDataDtos);
        }
    }

    @DeleteMapping("/delete-booking-tour-hotel-by-tour-detail-id-and-hotel-id/{tourDetailId}/{hotelId}")
    public ResponseObject delete(@PathVariable String tourDetailId,
                                 @PathVariable String hotelId) {
        try {
            List<OrderHotels> orderHotelsList = bookingTourHotelService.findOrderHotelByTourDetailIdAndHotelId(tourDetailId, hotelId);
            for (OrderHotels orderHotel : orderHotelsList) {
                orderHotel.setOrderStatus(2);
                bookingTourHotelService.update(orderHotel);
            }
            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @PutMapping("/pay-booking-tour-hotel-by-tour-detail-id-and-hotel-id")
    public ResponseObject pay(@RequestParam(required = false) String tourDetailId,
                              @RequestParam(required = false) String hotelId,
                              @RequestParam(required = false) String payment) {
        try {
            List<OrderHotels> orderHotelsList = bookingTourHotelService.findOrderHotelByTourDetailIdAndHotelId(tourDetailId, hotelId);
            for (OrderHotels orderHotel : orderHotelsList) {
                orderHotel.setOrderStatus(1);
                orderHotel.setPaymentMethod(payment);
                bookingTourHotelService.update(orderHotel);
            }
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", e.getMessage());
        }
    }

}
