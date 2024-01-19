package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitLocationsRepository extends JpaRepository<VisitLocations, Integer> {

    VisitLocations findByUserId(int userId);
}