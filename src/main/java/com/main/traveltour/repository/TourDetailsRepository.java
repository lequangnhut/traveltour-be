package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDetailsRepository extends JpaRepository<TourDetails, Integer> {
}