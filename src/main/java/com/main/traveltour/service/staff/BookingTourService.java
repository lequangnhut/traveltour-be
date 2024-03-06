package com.main.traveltour.service.staff;

import com.main.traveltour.entity.BookingTours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookingTourService {
    Page<BookingTours> getAll(Pageable pageable);

    BookingTours findById(String bookingTourId);

    Page<BookingTours> findBySearchTerm(String searchTerm, Pageable pageable);

    void update(BookingTours bookingTour);
}
