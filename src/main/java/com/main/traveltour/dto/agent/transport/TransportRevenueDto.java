package com.main.traveltour.dto.agent.transport;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransportRevenueDto {
    private List<BigDecimal> revenue;
    private List<BigDecimal> lastYearRevenue;
}
