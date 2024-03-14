package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.RoomTypes;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.main.traveltour.entity.OrderHotelDetails}
 */
@Data
public class OrderHotelDetailsDto implements Serializable {
    int id;
    String orderHotelId;
    String roomTypeId;
    String customerName;
    String customerEmail;
    Integer amount;
    BigDecimal unitPrice;
    RoomTypes roomTypes;
}