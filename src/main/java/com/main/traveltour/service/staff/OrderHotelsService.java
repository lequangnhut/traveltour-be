package com.main.traveltour.service.staff;

import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderHotelsService {
    String maxCodeTourId();

    OrderHotels save(OrderHotels orderHotels);

    void saveOrderHotelCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);

    void saveOrderHotelPaymentOnlineCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);

    Page<OrderHotels> getAllByUserId(Integer orderStatus, String email, Pageable pageable);

    OrderHotels findById(String id);

    Page<OrderHotels> findOrderByIds(List<String> orderIds, Pageable pageable);

}
