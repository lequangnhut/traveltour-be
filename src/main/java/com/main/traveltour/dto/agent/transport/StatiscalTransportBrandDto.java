package com.main.traveltour.dto.agent.transport;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatiscalTransportBrandDto {
    private Integer year;
    private Integer month;
    private String formLocation;
    private String toLocation;
    private BigDecimal maxAmount;
}
