package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.repository.BookingTourCustomersRepository;
import com.main.traveltour.service.staff.BookingTourCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookingTourCustomerServiceImpl implements BookingTourCustomerService {

    @Autowired
    private BookingTourCustomersRepository repo;

    @Override
    public Page<BookingTourCustomers> findBySearchTermAndTourDetailId(String tourDetailId, String searchTerm, Pageable pageable) {
        return repo.findBySearchTermAndTourDetailId(tourDetailId, searchTerm, pageable);
    }

    @Override
    public Optional<BookingTourCustomers> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public BookingTourCustomers findByCustomerPhone(String customerPhone) {
        return repo.findByCustomerPhone(customerPhone);
    }

    @Override
    public List<BookingTourCustomers> findByTourDetailId(String tourDetailId) {
        return repo.findByTourDetailId(tourDetailId);
    }

    @Override
    public BookingTourCustomers create(BookingTourCustomers bookingTourCustomers) {
        return repo.save(bookingTourCustomers);
    }

    @Override
    public BookingTourCustomers update(BookingTourCustomers bookingTourCustomers) {
        return repo.save(bookingTourCustomers);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Long countCustomer() {
        return repo.countBookingTourCustomers();
    }
}
