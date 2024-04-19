package com.main.traveltour.dto.agent.hotel;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LastYearRevenueDto {
    private Integer year;
    private Integer month;
    private BigDecimal totalRoomType;
}
