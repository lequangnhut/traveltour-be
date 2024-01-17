package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourDetailsRepository extends JpaRepository<TourDetails, Integer> {
}