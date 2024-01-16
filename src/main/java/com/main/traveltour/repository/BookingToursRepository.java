package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingToursRepository extends JpaRepository<BookingTours, Integer> {
}