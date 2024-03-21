package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.staff.BookingToursDto;
import com.main.traveltour.dto.staff.TransportationSchedulesDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.BookingTourService;
import com.main.traveltour.service.staff.HotelServiceService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour/")
public class BookingTourAPI {

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private EmailService emailService;

    @GetMapping("find-all-booking-tour")
    public ResponseObject getAllBookingTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<BookingTours> bookingTours = searchTerm != null && !searchTerm.isEmpty()
                ? bookingTourService.findBySearchTerm(orderStatus, searchTerm, PageRequest.of(page, size, sort))
                : bookingTourService.getAll(orderStatus, PageRequest.of(page, size, sort));

        Page<BookingToursDto> bookingToursDtos = bookingTours.map(bookingTour -> EntityDtoUtils.convertToDto(bookingTour, BookingToursDto.class));

        if (bookingToursDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bookingToursDtos);
        }
    }

    @PutMapping("/update-booking-tour/{id}")
    public ResponseObject update(@PathVariable String id) {
        try {
            BookingTours bookingTour = bookingTourService.findById(id);
            bookingTour.setOrderStatus(1);
            bookingTourService.update(bookingTour);

            com.main.traveltour.dto.customer.booking.BookingToursDto bookingToursDtos = EntityDtoUtils.convertToDto(bookingTour, com.main.traveltour.dto.customer.booking.BookingToursDto.class);

            BookingDto bookingDto = new BookingDto();
            bookingDto.setBookingToursDto(bookingToursDtos);
            emailService.queueEmailBookingTourInvoices(bookingDto);

            return new ResponseObject("200", "Cập nhật thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("/delete-booking-tour/{id}")
    public ResponseObject delete(@PathVariable String id) {
        try {
            BookingTours bookingTour = bookingTourService.findById(id);
            bookingTour.setOrderStatus(2);
            bookingTourService.update(bookingTour);
            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

}
