package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.infomation.CancelBookingTourDTO;
import com.main.traveltour.dto.staff.BookingToursDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.CancelOrders;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.restcontroller.customer.bookingtour.service.BookingTourAPIService;
import com.main.traveltour.service.customer.CancelOrdersService;
import com.main.traveltour.service.staff.BookingTourService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour/")
public class BookingTourAPI {

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private BookingTourAPIService bookingTourAPIService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private CancelOrdersService cancelOrdersService;

    @Autowired
    private EmailService emailService;

    @GetMapping("find-all-booking-tour")
    public ResponseObject getAllBookingTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String dateSort,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String searchTerm) {
        Sort sort;

        if (dateSort != null && !dateSort.isEmpty()) {
            sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(dateSort).ascending()
                    : Sort.by(dateSort).descending();
        } else {
            sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }

        Page<BookingTours> bookingTours = searchTerm != null && !searchTerm.isEmpty()
                ? bookingTourService.findBySearchTerm(orderStatus, searchTerm, PageRequest.of(page, size, sort))
                : bookingTourService.getAll(orderStatus, PageRequest.of(page, size, sort));

        Page<BookingToursDto> bookingToursDto = bookingTours.map(bookingTour -> EntityDtoUtils.convertToDto(bookingTour, BookingToursDto.class));

        if (bookingToursDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bookingToursDto);
        }
    }

    @PutMapping("/update-booking-tour/{id}")
    public ResponseObject update(@PathVariable String id) {
        try {
            BookingTours bookingTour = bookingTourService.findById(id);
            bookingTour.setOrderStatus(1);
            bookingTourService.update(bookingTour);

            bookingTourAPIService.createInvoices(bookingTour.getId());
            bookingTourAPIService.createContracts(bookingTour.getId());

            com.main.traveltour.dto.customer.booking.BookingToursDto bookingToursDtos = EntityDtoUtils.convertToDto(bookingTour, com.main.traveltour.dto.customer.booking.BookingToursDto.class);

            BookingDto bookingDto = new BookingDto();
            bookingDto.setBookingToursDto(bookingToursDtos);
            emailService.queueEmailBookingTourInvoices(bookingDto);

            return new ResponseObject("200", "Cập nhật thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("/delete-booking-tour")
    public ResponseObject delete(@RequestParam String bookingTourId,
                                 @RequestParam String orderNoted,
                                 @RequestParam Boolean whoCancelled) {
        try {
            if (whoCancelled) { // true là khách hàng || false là nhân viên
                customerCancelled(bookingTourId, orderNoted);
            } else {
                staffCancelled(bookingTourId, orderNoted);
            }

            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    public void customerCancelled(String bookingTourId, String orderNoted) {
        int deposit;
        BigDecimal moneyBack = null;

        BookingTours bookingTour = bookingTourService.findById(bookingTourId);
        bookingTour.setOrderNote(orderNoted);
        bookingTour.setOrderStatus(2);
        bookingTour.setDateCancelled(new Timestamp(System.currentTimeMillis()));
        bookingTourService.update(bookingTour);

        TourDetails tourDetails = tourDetailsService.findById(bookingTour.getTourDetailId());

        //Tìm ra số lượng khách và total
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int capacityAdult = bookingTour.getCapacityAdult();
        int capacityKid = bookingTour.getCapacityKid();
        int unitPrice = unitPriceDecimal.intValue();
        BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

        //Tìm giá trị cọc theo ngày
        Date currentDate = new Date();
        Date departureDate = tourDetails.getDepartureDate();
        long currentDateTime = currentDate.getTime();
        long departureDateTime = departureDate.getTime();
        long diffInDays = (departureDateTime / (1000 * 60 * 60 * 24)) - (currentDateTime / (1000 * 60 * 60 * 24));

        if (bookingTour.getPaymentMethod() == 0 && bookingTour.getOrderStatus() == 0) {
            deposit = 0;
            moneyBack = orderTotal;
        } else {
            if (diffInDays >= 30) {
                deposit = 1;
            } else if (diffInDays >= 26 && diffInDays <= 29) {
                deposit = 5;
            } else if (diffInDays >= 15 && diffInDays <= 25) {
                deposit = 30;
            } else if (diffInDays >= 8 && diffInDays <= 14) {
                deposit = 50;
            } else if (diffInDays >= 2 && diffInDays <= 7) {
                deposit = 80;
            } else if (diffInDays <= 1) {
                deposit = 100;
            } else {
                deposit = 0;
                moneyBack = orderTotal;
            }
        }

        BigDecimal cocPercentage = BigDecimal.valueOf(deposit);
        BigDecimal cocAmount = orderTotal.multiply(cocPercentage).divide(BigDecimal.valueOf(100));
        moneyBack = orderTotal.subtract(cocAmount);

        //Trả ghế lại cho Tour Details
        Integer totalAmountBook = bookingTour.getCapacityAdult() + bookingTour.getCapacityKid();
        Integer booked = tourDetails.getBookedSeat();
        tourDetails.setBookedSeat(booked - totalAmountBook);
        tourDetailsService.save(tourDetails);

        CancelBookingTourDTO bookingToursDto = EntityDtoUtils.convertToDto(bookingTour, CancelBookingTourDTO.class);
        bookingToursDto.setCoc(deposit);
        bookingToursDto.setMoneyBack(moneyBack);
        bookingToursDto.setReasonNote(orderNoted);

        //Entity của các đơn hủy
        CancelOrders cancelOrders = new CancelOrders();
        cancelOrders.setOrderId(bookingToursDto.getId());
        cancelOrders.setCategogy(0);
        cancelOrders.setDepositValue(bookingToursDto.getCoc());
        BigDecimal price = bookingToursDto.getOrderTotal().subtract(bookingToursDto.getMoneyBack());
        cancelOrders.setDepositPrice(price);
        cancelOrders.setDateCreated(new Timestamp(System.currentTimeMillis()));
        cancelOrdersService.save(cancelOrders);

        //Gửi mail
        emailService.queueEmailCustomerCancelTour(bookingToursDto);
    }

    public void staffCancelled(String bookingTourId, String orderNoted) {
        BookingTours bookingTour = bookingTourService.findById(bookingTourId);
        bookingTour.setOrderNote(orderNoted);
        bookingTour.setOrderStatus(2);
        bookingTour.setDateCancelled(new Timestamp(System.currentTimeMillis()));
        bookingTourService.update(bookingTour);

        TourDetails tourDetails = tourDetailsService.findById(bookingTour.getTourDetailId());
        //Trả ghế lại cho Tour Details
        Integer totalAmountBook = bookingTour.getCapacityAdult() + bookingTour.getCapacityKid();
        Integer booked = tourDetails.getBookedSeat();
        tourDetails.setBookedSeat(booked - totalAmountBook);
        tourDetailsService.save(tourDetails);

        CancelBookingTourDTO bookingToursDto = EntityDtoUtils.convertToDto(bookingTour, CancelBookingTourDTO.class);
        bookingToursDto.setCoc(0);
        bookingToursDto.setMoneyBack(BigDecimal.valueOf(0));
        bookingToursDto.setReasonNote(orderNoted);

        //Entity của các đơn hủy
        CancelOrders cancelOrders = new CancelOrders();
        cancelOrders.setOrderId(bookingToursDto.getId());
        cancelOrders.setCategogy(0);
        cancelOrders.setDepositValue(bookingToursDto.getCoc());
        cancelOrders.setDepositPrice(BigDecimal.valueOf(0));
        cancelOrders.setDateCreated(new Timestamp(System.currentTimeMillis()));
        cancelOrdersService.save(cancelOrders);

        //Gửi mail
        emailService.queueEmailStaffCancelTour(bookingToursDto);
    }
}
