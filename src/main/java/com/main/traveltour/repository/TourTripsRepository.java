package com.main.traveltour.repository;

import com.main.traveltour.entity.TourTrips;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourTripsRepository extends JpaRepository<TourTrips, Integer> {
}