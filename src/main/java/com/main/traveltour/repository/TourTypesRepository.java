package com.main.traveltour.repository;

import com.main.traveltour.entity.TourTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourTypesRepository extends JpaRepository<TourTypes, Integer> {
}