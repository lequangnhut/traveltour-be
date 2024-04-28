package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;

import java.sql.Timestamp;
import java.util.List;

public interface OrderHotelDetailService {
    void save(OrderHotelDetails orderHotelDetails);

    void saveOrderHotelDetailsCustomer(OrderHotelDetails orderHotelDetails);

    List<OrderHotelDetails> findByOrderHotelId(String orderHotelsId);

    Boolean getTotalBookedRooms(String roomTypeId, Timestamp checkInDate, Timestamp checkOutDate, Integer amount);

    List<OrderHotelDetails> findOrderHotelDetailsByRoomTypeIds(List<String> roomTypeIds);

    OrderHotelDetails findOrderHotelDetailsByRoomTypeId(String roomTypeId);

    List<String> findOrderHotelByIdsAfterTimestamp(List<String> orderHotelIds, long l);
}
