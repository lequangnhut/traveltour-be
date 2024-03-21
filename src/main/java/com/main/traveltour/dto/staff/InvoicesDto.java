package com.main.traveltour.dto.staff;

import com.main.traveltour.entity.BookingTours;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InvoicesDto {
    String id;
    String bookingTourId;
    Timestamp dateCreated;
    BookingTours bookingToursByBookingTourId;
}