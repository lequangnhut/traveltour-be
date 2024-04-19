package com.main.traveltour.dto.agent.hotel;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StatisticalBookingHotelDto {
    private String id;
    private String roomTypeName;
    private Integer year;
    private Integer month;
    private Integer orderStatus;
    private Integer countRoomType;
}
