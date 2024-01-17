package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTourCustomers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingTourCustomersRepository extends JpaRepository<BookingTourCustomers, Integer> {
}