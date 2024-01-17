package com.main.traveltour.repository;

import com.main.traveltour.entity.TourTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourTypesRepository extends JpaRepository<TourTypes, Integer> {
}