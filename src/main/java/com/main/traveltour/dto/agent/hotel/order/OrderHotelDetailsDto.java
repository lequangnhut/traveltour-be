package com.main.traveltour.dto.agent.hotel.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.RoomTypes;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderHotelDetailsDto {
    private int id;
    private String orderHotelId;
    private String roomTypeId;
    private String customerName;
    private String customerEmail;
    private Integer amount;
    private BigDecimal unitPrice;
    private OrderHotels orderHotelsByOrderHotelId;
    private RoomTypes roomTypesByRoomTypeId;
}
