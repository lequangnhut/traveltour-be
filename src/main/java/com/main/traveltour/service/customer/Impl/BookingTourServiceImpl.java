package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.repository.BookingTourCustomersRepository;
import com.main.traveltour.repository.BookingToursRepository;
import com.main.traveltour.service.customer.BookingTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingTourServiceImpl implements BookingTourService {

    @Autowired
    private BookingToursRepository bookingToursRepository;

    @Autowired
    private BookingTourCustomersRepository bookingTourCustomersRepository;

    @Override
    public void saveBookingTour(BookingTours bookingTours) {
        bookingToursRepository.save(bookingTours);
    }

    @Override
    public void saveBookingTourCustomer(BookingTourCustomers bookingTourCustomers) {
        bookingTourCustomersRepository.save(bookingTourCustomers);
    }
}
