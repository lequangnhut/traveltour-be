package com.main.traveltour.dto.staff;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVisitDetailsDto {
    int id;
    String orderVisitId;
    Integer visitLocationTicketId;
    Integer amount;
    BigDecimal unitPrice;
}