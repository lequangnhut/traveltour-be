package com.main.traveltour.dto.staff;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TransportationSearchDto {
    String fromLocation;
    String toLocation;
    Timestamp departureTime;
    Timestamp arrivalTime;
    Integer amountSeat;
}
