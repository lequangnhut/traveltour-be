package com.main.traveltour.restcontroller.customer.bookingtransport.vnpay;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.restcontroller.customer.bookingtransport.service.BookingTransportAPIService;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.Base64Utils;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.SessionAttr;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/transport/")
public class BookingTransportVNPayCusAPI {

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private BookingTransportAPIService bookingTransportAPIService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VNPayService vnPayService;

    /**
     * phương thức gửi api
     *
     * @param scheduleId     mã chuyến đi
     * @param orderInfo      mã đơn hàng
     * @param amountTicket   số lượng
     * @param seatNumber     mã ghế
     * @param orderTransport đối tượng lưu db
     */
    @PostMapping("vnpay/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("scheduleId") String scheduleId,
                                                                 @RequestParam("orderInfo") String orderInfo,
                                                                 @RequestParam("amountTicket") int amountTicket,
                                                                 @RequestParam("seatNumber") List<Integer> seatNumber,
                                                                 @RequestBody OrderTransportationsDto orderTransport) {
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(scheduleId);
        BigDecimal orderTotal = new BigDecimal(amountTicket).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        baseUrl += "/api/v1/customer/transport/vnpay/success-payment";
        String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderInfo, baseUrl);

        SessionAttr.ORDER_TRANSPORTATIONS_DTO = orderTransport;
        SessionAttr.SEAT_NUMBER = seatNumber;

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * phương thức lưu db khi giao dịch thành công
     */
    @GetMapping("vnpay/success-payment")
    private String successBookingTransportVNPAY(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        List<Integer> seatNumber = SessionAttr.SEAT_NUMBER;
        OrderTransportations orderTransportationSuccess = null;
        OrderTransportationsDto orderTransportationsDto = SessionAttr.ORDER_TRANSPORTATIONS_DTO;
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(orderTransportationsDto.getTransportationScheduleId());

        try {
            Integer userId = orderTransportationsDto.getUserId();
            BigDecimal orderTotal = new BigDecimal(orderTransportationsDto.getAmountTicket()).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

            // nếu thành công
            if (paymentStatus == 1) {
                OrderTransportations orderTransportations;
                // nếu người dùng đăng nhập
                if (userId != null) {
                    orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                    orderTransportations.setOrderTotal(orderTotal);
                    orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    orderTransportations.setOrderStatus(1); // đã thanh toán
                    orderTransportationSuccess = orderTransportService.save(orderTransportations);

                    schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket()); // set chổ ngồi đã đặt trong lịch trình
                    transportationScheduleService.save(schedules);

                    bookingTransportAPIService.createOrderDetailScheduleSeat(schedules, orderTransportations.getId(), seatNumber);
                } else {
                    // nếu người dùng không đăng nhập
                    orderTransportationSuccess = bookingTransportAPIService.createUserPayment(orderTransportationsDto, seatNumber, 1); // đã thanh toán
                }
            } else {
                // nếu thất bại
                OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                orderTransportations.setOrderTotal(orderTotal);
                orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                orderTransportations.setOrderStatus(2); // thất bại
                orderTransportations.setOrderNote("Thanh toán thất bại"); // nếu thất bại sẽ là lí do hủy
                orderTransportationSuccess = orderTransportService.save(orderTransportations);
            }

            emailService.queueEmailCustomerBookingTransport(orderTransportationsDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert orderTransportationSuccess != null;
        String orderStatusBase64 = Base64Utils.encodeData(String.valueOf(orderTransportationSuccess.getOrderStatus()));
        return "redirect:" + DomainURL.FRONTEND_URL + "/drive-move/drive-transport-detail/booking-confirmation/booking-successfully?orderStatus=" + orderStatusBase64;
    }
}
