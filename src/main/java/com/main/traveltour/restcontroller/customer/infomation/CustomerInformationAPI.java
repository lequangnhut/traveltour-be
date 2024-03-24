package com.main.traveltour.restcontroller.customer.infomation;


import com.main.traveltour.dto.customer.infomation.*;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.admin.TransportationSeatServiceAD;
import com.main.traveltour.service.agent.OrderTransportDetailService;
import com.main.traveltour.service.customer.OrderVehicleDetailsService;
import com.main.traveltour.service.staff.*;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
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
    private OrderVehicleDetailsService orderVehicleDetailsService;

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @Autowired
    private OrderVisitLocationDetailService orderVisitLocationDetailService;

    @Autowired
    TransportationSeatServiceAD transportationSeatServiceAD;


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

    @GetMapping("find-order-detail-by-ordersId/{orderHotelsId}")
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

    @GetMapping("find-trans-detail-by-ordersId/{orderId}")
    public ResponseObject findOrderTransDetails(@PathVariable String orderId) {
        try {
            List<OrderTransportationDetails> orderTransportationDetailsList = orderVehicleDetailsService.findByOrderId(orderId);
            List<OrderTransportationDetailsDto> orderTransportationDetailsDtos = EntityDtoUtils.convertToDtoList(orderTransportationDetailsList, OrderTransportationDetailsDto.class);

            return new ResponseObject("200", "Có nè", orderTransportationDetailsDtos);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
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

    @GetMapping("find-visit-detail-by-ordersId/{orderId}")
    public ResponseObject findOrderVisitDetails(@PathVariable String orderId) {
        try {
            List<OrderVisitDetails> orderVisitDetails = orderVisitLocationDetailService.findByOrderVisitId(orderId);
            List<OrderVisitDetailsDto> orderVisitDetailsDtos = EntityDtoUtils.convertToDtoList(orderVisitDetails, OrderVisitDetailsDto.class);

            return new ResponseObject("200", "Có nè", orderVisitDetailsDtos);
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @DeleteMapping("delete-booking-tour-customer/{id}")
    public ResponseObject delete(@PathVariable String id) {
        try {
            int coc;
            BigDecimal moneyBack = null;

            BookingTours bookingTour = bookingTourService.findById(id);
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
                coc = 0;
                moneyBack = orderTotal;
            } else {
                if (diffInDays >= 30) {
                    coc = 1;
                } else if (diffInDays >= 26 && diffInDays <= 29) {
                    coc = 5;
                } else if (diffInDays >= 15 && diffInDays <= 25) {
                    coc = 30;
                } else if (diffInDays >= 8 && diffInDays <= 14) {
                    coc = 50;
                } else if (diffInDays >= 2 && diffInDays <= 7) {
                    coc = 80;
                } else if (diffInDays >= 0 && diffInDays <= 1) {
                    coc = 100;
                } else {
                    coc = 0;
                    moneyBack = orderTotal;
                }
            }
            BigDecimal cocPercentage = BigDecimal.valueOf(coc);
            BigDecimal cocAmount = orderTotal.multiply(cocPercentage).divide(BigDecimal.valueOf(100));
            moneyBack = orderTotal.subtract(cocAmount);

             //Trả ghế lại cho Tour Details
            Integer totalAmountBook = bookingTour.getCapacityAdult() + bookingTour.getCapacityKid() + bookingTour.getCapacityBaby();
            Integer booked = tourDetails.getBookedSeat();
            //Lưu trạng thái mới đơn hàng
            bookingTour.setOrderStatus(2);
            bookingTourService.update(bookingTour);
            //Lưu ghế lại cho Tour Details
            tourDetails.setBookedSeat(booked - totalAmountBook);
            tourDetailsService.save(tourDetails);
            //Trans qua DTO
            CancelBookingTourDTO bookingToursDto = EntityDtoUtils.convertToDto(bookingTour, CancelBookingTourDTO.class);
            bookingToursDto.setCoc(coc);
            bookingToursDto.setMoneyBack(moneyBack);
//            System.out.println(bookingToursDto.getCoc());
//            System.out.println(bookingToursDto.getMoneyBack());
            //Gửi mail
            emailService.queueEmailCustomerCancelTour(bookingToursDto);


            return new ResponseObject("200", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }
    @DeleteMapping("delete-booking-hotel-customer/{id}")
    public ResponseObject deleteHotelOrder(@PathVariable String id) {
        try {
            boolean notFree = false;
            String name = null;
            int coc;
            BigDecimal moneyBack = null;

            OrderHotels orderHotels = orderHotelsService.findById(id);
            List<OrderHotelDetails> orderHotelDetails = orderHotelDetailService.findByOrderHotelId(orderHotels.getId());
            List<OrderHotelDetailsDto> orderHotelDetailsDto = EntityDtoUtils.convertToDtoList(orderHotelDetails, OrderHotelDetailsDto.class);

            for (OrderHotelDetailsDto orderDetail : orderHotelDetailsDto) {
                RoomTypes roomType = orderDetail.getRoomTypes();
                Hotels hotelFound = hotelServiceService.getHotelsById(roomType.getHotelId());
                name = hotelFound.getHotelName();
                if (roomType.getFreeCancellation() == false) {
                    notFree = true;
                    break;
                }
            }

            Date currentDate = new Date();
            Date departureDate = orderHotels.getCheckIn();
            long currentDateTime = currentDate.getTime();
            long departureDateTime = departureDate.getTime();
            long diffInDays = (departureDateTime - currentDateTime) / (1000 * 60 * 60 * 24);

            if ((orderHotels.getPaymentMethod().equals("TTTT") && orderHotels.getOrderStatus() == 0) || notFree == false) {
                coc = 0;
                moneyBack = orderHotels.getOrderTotal();
            } else {
                if (diffInDays >= 2 && diffInDays <= 4) {
                    coc = 50;
                } else if (diffInDays >= 0 && diffInDays <= 1) {
                    coc = 100;
                } else {
                    coc = 0;
                    moneyBack = orderHotels.getOrderTotal();
                }
            }

            BigDecimal cocPercentage = BigDecimal.valueOf(coc); // Chuyển phần trăm phí hủy thành BigDecimal
            BigDecimal cocAmount = (orderHotels.getOrderTotal()).multiply(cocPercentage).divide(BigDecimal.valueOf(100)); // Tính số tiền tương ứng với phí hủy
            moneyBack = (orderHotels.getOrderTotal()).subtract(cocAmount); // Trừ số tiền phí hủy từ tổng tiền

            orderHotels.setOrderStatus(2);
            orderHotelsService.save(orderHotels);

            CancelOrderHotelsDto cancelOrderHotelsDto = EntityDtoUtils.convertToDto(orderHotels, CancelOrderHotelsDto.class);
            cancelOrderHotelsDto.setCoc(coc);
            cancelOrderHotelsDto.setMoneyBack(moneyBack);

            emailService.queueEmailCustomerCancelHotel(cancelOrderHotelsDto);

            return new ResponseObject("200", "Xóa thành công", cancelOrderHotelsDto);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
        }
    }

    @DeleteMapping("delete-booking-visit-customer/{id}")
    public ResponseObject deleteVisitOrder(@PathVariable String id) {
//        try {
//            OrderVisits orderVisits = orderVisitLocationService.findById(id);
//
//            orderVisits.setOrderStatus(2);
//            orderVisitLocationService.save(orderVisits);
//
//            OrderVisitsDto orderVisitsDto = EntityDtoUtils.convertToDto(orderVisits, OrderVisitsDto.class);
//
//            emailService.queueEmailCustomerCancelVisit(orderVisitsDto);
//
//            return new ResponseObject("200", "Xóa thành công", orderVisitsDto);
//        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
//        }
    }

    @DeleteMapping("delete-booking-trans-customer/{id}")
    public ResponseObject deleteTransOrder(@PathVariable String id) {
        try {
            int coc;
            BigDecimal moneyBack = null;

            OrderTransportations orderTransportations = orderTransportationService.findById(id);
            List<OrderTransportationDetails> orderTransportationDetailsList = orderVehicleDetailsService.findByOrderId(orderTransportations.getId());
            List<OrderTransportationDetailsDto> orderTransportationDetailsDtos = EntityDtoUtils.convertToDtoList(orderTransportationDetailsList, OrderTransportationDetailsDto.class);

            Date currentDate = new Date();
            Date departureDate = orderTransportations.getTransportationSchedulesByTransportationScheduleId().getDepartureTime();
            long currentDateTime = currentDate.getTime();
            long departureDateTime = departureDate.getTime();
            long diffInDays = (departureDateTime - currentDateTime) / (1000 * 60 * 60 * 24);

//            System.out.println(diffInDays);

            if (orderTransportations.getPaymentMethod() == 0 && orderTransportations.getOrderStatus() == 0) {
                coc = 0;
                moneyBack = orderTransportations.getOrderTotal();
            } else {
                if (diffInDays >= 2 && diffInDays <= 3) {
                    coc = 30;
                } else if (diffInDays >= 0 && diffInDays <= 1) {
                    coc = 70;
                } else {
                    coc = 0;
                    moneyBack = orderTransportations.getOrderTotal();
                }
            }

            BigDecimal cocPercentage = BigDecimal.valueOf(coc);
            BigDecimal cocAmount = (orderTransportations.getOrderTotal()).multiply(cocPercentage).divide(BigDecimal.valueOf(100));
            moneyBack = (orderTransportations.getOrderTotal()).subtract(cocAmount);

            orderTransportations.setOrderStatus(2);
            orderTransportationService.save(orderTransportations);

            List<TransportationScheduleSeats> transportationScheduleSeats = transportationSeatServiceAD.findSeatByOrderTd(orderTransportations.getId());
            for (TransportationScheduleSeats seat : transportationScheduleSeats) {
                seat.setIsBooked(false);
                transportationSeatServiceAD.save(seat);
            }

            CancelOrderTransportationsDto cancelOrderTransportationsDto = EntityDtoUtils.convertToDto(orderTransportations, CancelOrderTransportationsDto.class);
            cancelOrderTransportationsDto.setCoc(coc);
            cancelOrderTransportationsDto.setMoneyBack(moneyBack);

//            System.out.println(cancelOrderTransportationsDto.getCoc());
//            System.out.println(cancelOrderTransportationsDto.getMoneyBack());

            emailService.queueEmailCustomerCancelTrans(cancelOrderTransportationsDto);

            return new ResponseObject("200", "Xóa thành công", cancelOrderTransportationsDto);
        } catch (Exception e) {
            return new ResponseObject("500", "Xóa thất bại", null);
      }
    }

}
