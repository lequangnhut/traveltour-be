package com.main.traveltour.dto.customer.booking;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link com.main.traveltour.entity.BookingTourCustomers}
 */
@Data
public class BookingTourCustomersDto implements Serializable {

    int id;

    String bookingTourId;

    String customerName;

    Date customerBirth;

    String customerPhone;
}