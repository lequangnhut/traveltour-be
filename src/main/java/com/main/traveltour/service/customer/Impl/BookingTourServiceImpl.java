package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.Contracts;
import com.main.traveltour.entity.Invoices;
import com.main.traveltour.repository.BookingTourCustomersRepository;
import com.main.traveltour.repository.BookingToursRepository;
import com.main.traveltour.repository.ContractsRepository;
import com.main.traveltour.repository.InvoicesRepository;
import com.main.traveltour.service.customer.BookingTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingTourServiceImpl implements BookingTourService {

    @Autowired
    private BookingToursRepository bookingToursRepository;

    @Autowired
    private BookingTourCustomersRepository bookingTourCustomersRepository;

    @Autowired
    private InvoicesRepository invoicesRepository;

    @Autowired
    private ContractsRepository contractsRepository;

    @Override
    public String findMaxCodeInvoices() {
        return invoicesRepository.findMaxCode();
    }

    @Override
    public String findMaxCodeContracts() {
        return contractsRepository.findMaxCode();
    }

    @Override
    public BookingTours findBookingTourById(String bookingTourId) {
        return bookingToursRepository.findById(bookingTourId);
    }

    @Override
    public void saveBookingTour(BookingTours bookingTours) {
        bookingToursRepository.save(bookingTours);
    }

    @Override
    public void saveBookingTourCustomer(BookingTourCustomers bookingTourCustomers) {
        bookingTourCustomersRepository.save(bookingTourCustomers);
    }

    @Override
    public void saveBookingInvoices(Invoices invoices) {
        invoicesRepository.save(invoices);
    }

    @Override
    public void saveBookingContracts(Contracts contracts) {
        contractsRepository.save(contracts);
    }
}
