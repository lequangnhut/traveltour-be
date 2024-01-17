package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHotelsRepository extends JpaRepository<OrderHotels, Integer> {
}