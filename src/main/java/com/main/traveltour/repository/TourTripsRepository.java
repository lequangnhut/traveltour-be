package com.main.traveltour.repository;

import com.main.traveltour.entity.TourTrips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourTripsRepository extends JpaRepository<TourTrips, Integer> {
}