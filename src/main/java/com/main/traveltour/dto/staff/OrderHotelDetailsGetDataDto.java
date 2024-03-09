package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.RoomTypes;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderHotelDetailsGetDataDto {
    int id;
    String orderHotelId;
    String roomTypeId;
    Integer amount;
    BigDecimal unitPrice;
    OrderHotels orderHotelsByOrderHotelId;
    RoomTypes roomTypesByRoomTypeId;
}