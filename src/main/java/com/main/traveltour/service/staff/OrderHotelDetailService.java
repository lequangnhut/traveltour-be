package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;

import java.util.List;

public interface OrderHotelDetailService {
    void save(OrderHotelDetails orderHotelDetails);

    void saveOrderHotelDetailsCustomer(OrderHotelDetails orderHotelDetails);

    List<OrderHotelDetails> findByOrderHotelId(String orderHotelsId);
}
