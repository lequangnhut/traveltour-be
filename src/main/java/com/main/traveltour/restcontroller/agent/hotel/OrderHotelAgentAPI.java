package com.main.traveltour.restcontroller.agent.hotel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.agent.hotel.order.OrderHotelDto;
import com.main.traveltour.dto.customer.infomation.CancelOrderHotelsDto;
import com.main.traveltour.dto.customer.infomation.OrderHotelsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderHotelAgentAPI {
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private OrderHotelsService orderHotelsService;
    @Autowired
    private OrderHotelDetailService orderHotelDetailService;
    @Autowired
    private EmailService emailService;

    /**
     * Phương thức tìm kiếm hóa đơn của khách sạn
     *
     * @param hotelId mã khách sạn
     * @return danh sách hóa đơn khách sạn
     */
    @GetMapping("/agent/order-hotel/findAllOrderHotel")
    public ResponseEntity<Page<OrderHotels>> findAllOrderHotel(
            @RequestParam("hotelId") String hotelId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(value = "sortField", defaultValue = "dateCreated", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) Sort.Direction sortDirection,
            @RequestParam(value = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam(value = "filter", required = false) Integer filter,
            @RequestParam(value = "orderStatus", required = false) Integer orderStatus
    ) {
        Sort.Direction defaultSortDirection = Sort.Direction.DESC;
        Sort.Direction finalSortDirection = sortDirection != null ? sortDirection : defaultSortDirection;
        Sort sort = sortField != null ? Sort.by(finalSortDirection, sortField) : Sort.by(defaultSortDirection, "id");
        LocalDate targetDateTime = null;
        if (filter != null) {
            switch (filter) {
                case 0:
                    break;
                case 1:
                    targetDateTime = LocalDate.now();
                    break;
                case 2:
                    targetDateTime = LocalDate.now().plusDays(1);
                    break;
                case 3:
                    targetDateTime = LocalDate.now().plusDays(2);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + filter);
            }
        }
        System.out.println(page + " " + size + " " + sortField + " " + sortDirection + " " + searchTerm + " " + filter + " " + orderStatus + " " + targetDateTime);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(orderHotelsService.findOrderHotelsByFilter(hotelId, targetDateTime, searchTerm, orderStatus, pageable));
    }

    @GetMapping("agent/order-hotel/findOrderHotelById")
    public ResponseEntity<OrderHotelDto> findOrderHotelById(@RequestParam("orderId") String orderId) {
        OrderHotelDto orderHotelsDto = orderHotelsService.findByOrderHotelId(orderId);
        return ResponseEntity.ok(orderHotelsDto);
    }

    @GetMapping("agent/order-hotel/confirmInvoiceByIdOrder")
    public ResponseEntity confirmInvoiceByIdOrder(@RequestParam("orderId") String orderId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        orderHotelsService.confirmInvoiceByIdOrder(orderId);
        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Xác nhận hóa đơn thành công, Vui lòng chuẩn bị phòng trước thời gian khách đến!")));
    }

    @GetMapping("agent/order-hotel/cancelInvoiceByIdOrder")
    public ResponseEntity cancelInvoiceByIdOrder(
            @RequestParam("orderId") String orderId,
            @RequestParam("cancelReason") String cancelReason) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        orderHotelsService.cancelInvoiceByIdOrder(orderId, cancelReason);

        OrderHotels orderHotels = orderHotelsService.findByIdOptional(orderId).orElseThrow(() -> {
            try {
                return new IllegalStateException(objectMapper.writeValueAsString(Collections.singletonMap("message", "Lỗi không tìm thấy mã hóa đơn!")));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        orderHotels.setOrderStatus(5);
        orderHotels.setOrderReason(cancelReason);
        CancelOrderHotelsDto cancelOrderHotelsDto = CancelOrderHotelsDto.builder()
                .id(orderHotels.getId())
                .userId(orderHotels.getUserId())
                .customerName(orderHotels.getCustomerName())
                .customerCitizenCard(orderHotels.getCustomerCitizenCard())
                .customerPhone(orderHotels.getCustomerPhone())
                .customerEmail(orderHotels.getCustomerEmail())
                .capacityAdult(orderHotels.getCapacityAdult())
                .capacityKid(orderHotels.getCapacityKid())
                .checkIn(orderHotels.getCheckIn())
                .checkOut(orderHotels.getCheckOut())
                .orderTotal(orderHotels.getOrderTotal())
                .paymentMethod(orderHotels.getPaymentMethod())
                .orderCode(orderHotels.getOrderCode())
                .dateCreated(orderHotels.getDateCreated())
                .orderStatus(orderHotels.getOrderStatus())
                .orderNote(orderHotels.getOrderNote())
                .reasonNote(orderHotels.getOrderReason())
                .orderHotelDetails(orderHotels.getOrderHotelDetailsById())
                .build();
        if (cancelOrderHotelsDto.getCustomerEmail() != null) {
            emailService.queueEmailCustomerCancelHotel(cancelOrderHotelsDto);
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Hủy hóa đơn hành công")));
    }
}
