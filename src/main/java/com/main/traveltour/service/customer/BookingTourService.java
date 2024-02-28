package com.main.traveltour.service.customer;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.BookingTours;

public interface BookingTourService {

    void saveBookingTour(BookingTours bookingTours);

    void saveBookingTourCustomer(BookingTourCustomers bookingTourCustomers);
}
