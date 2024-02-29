package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingToursRepository extends JpaRepository<BookingTours, Integer> {

    BookingTours findById(String bookingTourId);
}