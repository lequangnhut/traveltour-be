package com.main.traveltour.repository;

import com.main.traveltour.entity.PlaceUtilities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceUtilitiesRepository extends JpaRepository<PlaceUtilities, Integer> {

    PlaceUtilities findById(int id);
}