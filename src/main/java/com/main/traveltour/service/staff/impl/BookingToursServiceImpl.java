package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.repository.BookingToursRepository;
import com.main.traveltour.service.staff.BookingTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingToursServiceImpl implements BookingTourService {

    @Autowired
    private BookingToursRepository repo;


    @Override
    public Page<BookingTours> getAll(Integer orderStatus, Pageable pageable) {
        return repo.findAllBookingTours(orderStatus, pageable);
    }

    public BookingTours findById(String bookingTourId) {
        return repo.findById(bookingTourId);
    }

    @Override
    public Page<BookingTours> findBySearchTerm(Integer orderStatus, String searchTerm, Pageable pageable) {
        return repo.findBySearchTerm(orderStatus, searchTerm, pageable);
    }

    @Override
    public void update(BookingTours bookingTour) {
        repo.save(bookingTour);
    }

    @Override
    public Page<BookingTours> getAllByUserId(Integer orderStatus, String email, Pageable pageable) {
        return repo.findAllBookingToursByUserId(orderStatus, email, pageable);
    }

    @Override
    public Optional<BookingTours> findByIdOptional(String id) {
        return Optional.ofNullable(repo.findById(id));
    }
}
