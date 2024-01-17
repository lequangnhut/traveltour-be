package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportationTypesRepository extends JpaRepository<TransportationTypes, Integer> {
}