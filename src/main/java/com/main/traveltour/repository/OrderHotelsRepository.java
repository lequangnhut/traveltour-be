package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderHotelsRepository extends JpaRepository<OrderHotels, Integer> {
    @Query("SELECT COALESCE(MAX(oh.id), 'OH0000') FROM OrderHotels oh")
    String maxCodeTourId();


}