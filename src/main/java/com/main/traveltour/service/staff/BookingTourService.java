package com.main.traveltour.service.staff;

import com.main.traveltour.entity.BookingTours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface BookingTourService {

    Page<BookingTours> getAll(Integer orderStatus, Pageable pageable);

    BookingTours findById(String bookingTourId);

    Page<BookingTours> findBySearchTerm(Integer orderStatus, String searchTerm, Pageable pageable);

    void update(BookingTours bookingTour);

    Page<BookingTours> getAllByUserId(Integer orderStatus, String email, Pageable pageable);

    Optional<BookingTours> findByIdOptional(String id);
}
