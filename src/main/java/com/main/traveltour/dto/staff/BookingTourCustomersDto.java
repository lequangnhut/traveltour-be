package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Date;

@Data
public class BookingTourCustomersDto {
    int id;
    String bookingTourId;
    String customerName;
    Date customerBirth;
    String customerPhone;
}