package com.main.traveltour.restcontroller.customer.bookingtour;

import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.restcontroller.customer.bookingtour.service.BookingTourAPIService;
import com.main.traveltour.service.customer.BookingTourService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-tour/")
public class BookingTourCusAPI {

    @Autowired
    private BookingTourAPIService bookingTourAPIService;

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("create-book-tour")
    private ResponseObject createBookingTour(@RequestBody BookingDto bookingDto) {
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());
        List<Map<String, String>> bookingTourCustomersDto = bookingDto.getBookingTourCustomersDto();

        Integer userId = bookingToursDto.getUserId();
        Integer totalAmountBook = bookingToursDto.getCapacityAdult() + bookingToursDto.getCapacityKid() + bookingToursDto.getCapacityBaby();

        try {
            if (userId != null) {
                BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
                int capacityAdult = bookingToursDto.getCapacityAdult();
                int capacityKid = bookingToursDto.getCapacityKid();
                int unitPrice = unitPriceDecimal.intValue();

                BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

                BookingTours bookingTourDto = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                bookingTourDto.setOrderTotal(orderTotal);
                bookingTourDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
                bookingTourDto.setOrderStatus(0); // 0: chờ thanh toán
                bookingTourService.saveBookingTour(bookingTourDto);

                bookingTourAPIService.createBookingTourCustomers(bookingToursDto.getId(), bookingTourCustomersDto);
                bookingTourAPIService.decreaseAmountTour(bookingTourDto.getTourDetailId(), totalAmountBook);
            } else {
                bookingTourAPIService.createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, 0); // chờ thanh toán
            }
            emailService.queueEmailBookingTour(bookingDto);
            return new ResponseObject("200", "Thành công", bookingDto);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }
}
