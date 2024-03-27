package com.main.traveltour.restcontroller.customer.bookingtransport.momo;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.momo.config.Environment;
import com.main.traveltour.configpayment.momo.enums.RequestType;
import com.main.traveltour.configpayment.momo.models.PaymentResponse;
import com.main.traveltour.configpayment.momo.processor.CreateOrderMoMo;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
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
public class BookingTransportMomoCusAPI {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private BookingTransportAPIService bookingTransportAPIService;

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private EmailService emailService;

    @PostMapping("momo/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderMomo(@RequestParam("seatNumber") List<Integer> seatNumber,
                                                                @RequestBody OrderTransportationsDto orderTransport) throws Exception {
        String scheduleId = orderTransport.getTransportationScheduleId();
        String bookingTransportId = orderTransport.getId();
        Integer amountTicket = orderTransport.getAmountTicket();

        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(scheduleId);
        BigDecimal orderTotal = new BigDecimal(amountTicket).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

        SessionAttr.ORDER_TRANSPORTATIONS_DTO = orderTransport;
        SessionAttr.SEAT_NUMBER = seatNumber;

        Map<String, Object> response = new HashMap<>();

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());

        String orderInfo = "Thanh Toan Don Hang #" + bookingTransportId;
        String returnURL = DomainURL.BACKEND_URL + "/api/v1/customer/transport/momo/success-payment";
        String notifyURL = DomainURL.BACKEND_URL + "/api/v1/customer/transport/momo/success-payment";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(orderTotal.intValue()), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
        assert captureWalletMoMoResponse != null;
        response.put("redirectUrl", captureWalletMoMoResponse.getPayUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("momo/success-payment")
    private String successBookingTransportMomo(HttpServletRequest request) {
        String payType = request.getParameter("payType");
        int paymentStatus = payType.equals("qr") ? 1 : 0;

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
                emailService.queueEmailCustomerBookingTransport(orderTransportationsDto);
            } else {
                // nếu thất bại
                OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                orderTransportations.setOrderTotal(orderTotal);
                orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                orderTransportations.setOrderStatus(2); // thất bại
                orderTransportations.setOrderNote("Thanh toán thất bại"); // nếu thất bại sẽ là lí do hủy
                orderTransportationSuccess = orderTransportService.save(orderTransportations);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert orderTransportationSuccess != null;
        String orderStatusBase64 = Base64Utils.encodeData(String.valueOf(orderTransportationSuccess.getOrderStatus()));
        return "redirect:" + DomainURL.FRONTEND_URL + "/drive-move/drive-transport-detail/booking-confirmation/booking-successfully?orderStatus=" + orderStatusBase64;
    }
}
