package com.main.traveltour.dto.staff;

import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Tours;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InvoicesDto {

    String id;

    String bookingTourId;

    Timestamp dateCreated;

    TourDetails tourDetailsByTourDetailId;

    Tours toursByTourId;

    BookingTours bookingToursByBookingTourId;
}