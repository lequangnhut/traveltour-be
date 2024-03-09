package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;

public interface OrderHotelDetailService {
    void save(OrderHotelDetails orderHotelDetails);

    void saveOrderHotelDetailsCustomer(OrderHotelDetails orderHotelDetails);
}
