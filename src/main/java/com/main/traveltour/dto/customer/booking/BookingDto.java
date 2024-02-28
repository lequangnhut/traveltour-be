package com.main.traveltour.dto.customer.booking;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class BookingDto implements Serializable {

    BookingToursDto bookingToursDto;

    List<Map<String, String>> bookingTourCustomersDto;
}
