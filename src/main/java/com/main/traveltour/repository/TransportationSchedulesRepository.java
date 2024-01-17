package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportationSchedulesRepository extends JpaRepository<TransportationSchedules, Integer> {
}