package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotelDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHotelDetailsRepository extends JpaRepository<OrderHotelDetails, Integer> {
}