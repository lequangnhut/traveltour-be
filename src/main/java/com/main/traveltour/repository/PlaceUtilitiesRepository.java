package com.main.traveltour.repository;

import com.main.traveltour.entity.PlaceUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceUtilitiesRepository extends JpaRepository<PlaceUtilities, Integer> {

    PlaceUtilities findById(int id);

    Page<PlaceUtilities> findByPlaceUtilitiesNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    PlaceUtilities findByPlaceUtilitiesName(String name);

    void deleteById(int id);

}