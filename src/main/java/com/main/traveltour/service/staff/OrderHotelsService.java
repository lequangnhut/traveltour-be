package com.main.traveltour.service.staff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.dto.agent.hotel.order.OrderHotelDto;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderHotelsService {
    String maxCodeTourId();

    OrderHotels save(OrderHotels orderHotels);

    void saveOrderHotelCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);
    void saveOrderHotelAgent(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);

    void saveOrderHotelPaymentOnlineCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);

    Page<OrderHotels> getAllByUserId(Integer orderStatus, String email, Pageable pageable);

    OrderHotels findById(String id);

    Optional<OrderHotels> findByIdOptional(String orderId);
    Page<OrderHotels> findOrderByIds(List<String> orderIds, Pageable pageable);

    List<Double> findStatisticalBookingHotel(Integer year, String hotelId);

    List<StatisticalBookingHotelDto> findStatisticalRoomTypeHotel(Integer year, String hotelId);

    HotelRevenueDto findHotelRevenueStatistics(Integer year, String hotelId);

    List<Integer> getAllOrderHotelYear();

    OrderHotelDto findByOrderHotelId(String orderId);

    void confirmInvoiceByIdOrder(String orderId) throws JsonProcessingException;

    void cancelInvoiceByIdOrder(String orderId, String cancelReason) throws JsonProcessingException;

    Page<OrderHotels> findOrderHotelsByFilter(String hotelId, LocalDate targetTimestamp, String searchTerm, Integer orderStatus, Pageable pageable);

    void checkOrderHotelExpires();
}
