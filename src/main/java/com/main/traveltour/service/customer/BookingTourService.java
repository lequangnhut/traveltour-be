package com.main.traveltour.service.customer;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.Contracts;
import com.main.traveltour.entity.Invoices;

public interface BookingTourService {

    String findMaxCodeInvoices();

    String findMaxCodeContracts();

    BookingTours findBookingTourById(String bookingTourId);

    void saveBookingTour(BookingTours bookingTours);

    void saveBookingTourCustomer(BookingTourCustomers bookingTourCustomers);

    void saveBookingInvoices(Invoices invoices);

    void saveBookingContracts(Contracts contracts);
}
