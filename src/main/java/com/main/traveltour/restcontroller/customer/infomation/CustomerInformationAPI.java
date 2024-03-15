package com.main.traveltour.restcontroller.customer.infomation;


import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.dto.customer.infomation.OrderHotelDetailsDto;
import com.main.traveltour.dto.customer.infomation.OrderTransportationsDto;
import com.main.traveltour.dto.customer.infomation.OrderVisitDetailsDto;
import com.main.traveltour.dto.customer.infomation.OrderVisitsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.staff.*;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/customer-order-booking/")
public class CustomerInformationAPI {

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @Autowired
    private BookingTourHotelService bookingTourHotelService;

    @Autowired
    private HotelServiceService hotelServiceService;

    @Autowired
    private RoomTypeServiceService roomTypeServiceService;

    @Autowired
    private OrderTransportationService orderTransportationService;

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @Autowired
    private OrderVisitLocationDetailService orderVisitLocationDetailService;



    @GetMapping("find-all-booking-tour/{userId}")
    public ResponseObject getAllBookingTourById(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Integer orderStatus,
            @PathVariable int userId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<BookingTours> bookingTours = bookingTourService.getAllByUserId(orderStatus, userId, PageRequest.of(page, size, sort));

        Page<com.main.traveltour.dto.staff.BookingToursDto> bookingToursDtos = bookingTours.map(bookingTour -> EntityDtoUtils.convertToDto(bookingTour, com.main.traveltour.dto.staff.BookingToursDto.class));

        if (bookingToursDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bookingToursDtos);
        }
    }

    @GetMapping("find-tour-detail-by-id/{id}")
    public ResponseObject findTourDetail(@PathVariable String id) {
        try {
            TourDetails tourDetails = tourDetailsService.findById(id);
            return new ResponseObject("200", "Có nè", tourDetails);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @DeleteMapping("delete-booking-tour-customer/{id}")
    public ResponseObject delete(@PathVariable String id) {
        try {
            BookingTours bookingTour = bookingTourService.findById(id);

            TourDetails tourDetails = tourDetailsService.findById(bookingTour.getTourDetailId());
            Integer totalAmountBook = bookingTour.getCapacityAdult() + bookingTour.getCapacityKid() + bookingTour.getCapacityBaby();
            Integer booked = tourDetails.getBookedSeat();

            bookingTour.setOrderStatus(2);
            bookingTourService.update(bookingTour);

            tourDetails.setBookedSeat(booked - totalAmountBook);
            tourDetailsService.save(tourDetails);

            BookingToursDto bookingToursDto = EntityDtoUtils.convertToDto(bookingTour, BookingToursDto.class);
            emailService.queueEmailCustomerCancelTour(bookingToursDto);


            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @GetMapping("find-all-booking-tour-hotel/{userId}")
    public ResponseObject getAllBookingTourHotel(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
                                                 @RequestParam(required = false) Integer orderHotelStatus, @PathVariable int userId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Hotels> hotels = bookingTourHotelService.findHotelByUserId(userId, orderHotelStatus, PageRequest.of(page, size, sort));

        if (hotels.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    @GetMapping("find-all-order-hotel/{userId}")
    public ResponseObject getAllBookingHotelByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
                                                     @RequestParam(required = false) Integer orderStatus, @PathVariable int userId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<OrderHotels> orderHotels = orderHotelsService.getAllByUserId(orderStatus, userId, PageRequest.of(page, size, sort));

        if (orderHotels.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderHotels);
        }
    }

    @GetMapping("find-hotel-by-room/{roomTypeId}")
    public ResponseObject findHotels(@PathVariable String roomTypeId) {
        try {
            Hotels hotels = hotelServiceService.findByRoomTypeId(roomTypeId);
            return new ResponseObject("200", "Có nè", hotels);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @GetMapping("find-room-by-roomId/{roomTypeId}")
    public ResponseObject findRoomTypes(@PathVariable String roomTypeId) {
        try {
            Optional<RoomTypes> roomTypesOptional = roomTypeServiceService.findById(roomTypeId);
            return new ResponseObject("200", "Có nè", roomTypesOptional);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @GetMapping("find-orderdetails-by-ordersId/{orderHotelsId}")
    public ResponseObject findOrderHotelsDetails(@PathVariable String orderHotelsId) {
        try {
            List<OrderHotelDetails> orderHotelDetails = orderHotelDetailService.findByOrderHotelId(orderHotelsId);
            List<OrderHotelDetailsDto> orderHotelDetailsDto = EntityDtoUtils.convertToDtoList(orderHotelDetails, OrderHotelDetailsDto.class);

            return new ResponseObject("200", "Có nè", orderHotelDetailsDto);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @GetMapping("find-all-order-trans/{userId}")
    public ResponseObject getAllBookingTransByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
                                                     @RequestParam(required = false) Integer orderStatus, @PathVariable int userId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<OrderTransportations> orderTransportations = orderTransportationService.findByUserIdAndStatus(orderStatus, userId, PageRequest.of(page, size, sort));
        Page<OrderTransportationsDto> orderTransportationsDtos = orderTransportations.map(orderTransportations1 -> EntityDtoUtils.convertToDto(orderTransportations1, OrderTransportationsDto.class));

        if (orderTransportationsDtos.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderTransportationsDtos);
        }
    }

    @GetMapping("find-all-order-visits/{userId}")
    public ResponseObject getAllBookingVisitsByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
                                                     @RequestParam(required = false) Integer orderStatus, @PathVariable int userId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<OrderVisits> orderVisits = orderVisitLocationService.findByUserIdAndStatus(orderStatus, userId, PageRequest.of(page, size, sort));
        Page<OrderVisitsDto> orderVisitsDtos = orderVisits.map(orderVisits1 -> EntityDtoUtils.convertToDto(orderVisits1, OrderVisitsDto.class));

        if (orderVisitsDtos.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderVisitsDtos);
        }
    }

    @GetMapping("find-visitdetails-by-ordersId/{orderId}")
    public ResponseObject findOrderVisitDetails(@PathVariable String orderId) {
        try {
            List<OrderVisitDetails> orderVisitDetails = orderVisitLocationDetailService.findByOrderVisitId(orderId);
            List<OrderVisitDetailsDto> orderVisitDetailsDtos = EntityDtoUtils.convertToDtoList(orderVisitDetails, OrderVisitDetailsDto.class);

            return new ResponseObject("200", "Có nè", orderVisitDetailsDtos);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }
}
