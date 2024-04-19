package com.main.traveltour.dto.agent.hotel;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HotelRevenueDto {
    private List<BigDecimal> revenue;
    private List<BigDecimal> lastYearRevenue;
}
