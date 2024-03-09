package com.main.traveltour.dto.customer.hotel;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDetailsHotelCustomerDto {
    private int id;
    private String orderHotelId;
    private String roomTypeId;
    private String customerName;
    private String customerEmail;
    private Integer amount;
    private BigDecimal unitPrice;
}
