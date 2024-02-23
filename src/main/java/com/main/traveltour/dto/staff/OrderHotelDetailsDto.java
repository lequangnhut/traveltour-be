package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderHotelDetailsDto {
    int id;
    String orderHotelId;
    String roomTypeId;
    Integer amount;
    BigDecimal unitPrice;
}