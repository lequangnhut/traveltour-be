package com.main.traveltour.restcontroller.customer.bookingtour.service;

import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.entity.BookingTours;

import java.util.List;
import java.util.Map;

public interface BookingTourAPIService {

    void createUser(BookingToursDto bookingToursDto, List<Map<String, String>> bookingTourCustomersDto, Integer totalAmountBook, Integer orderStatus);

    void createBookingTour(BookingToursDto bookingToursDto, BookingTours bookingTours, List<Map<String, String>> bookingTourCustomersDto, Integer orderStatus);

    void createBookingTourCustomers(String bookingTourId, List<Map<String, String>> bookingTourCustomersDto);

    void decreaseAmountTour(String tourDetailId, Integer totalAmountBook);

    void createInvoices(String bookingTourId);

    void createContracts(String bookingTourId);
}
