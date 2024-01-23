package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransportationBrandsRepository extends JpaRepository<TransportationBrands, Integer> {

    @Query(value = "SELECT MAX(trans.id) FROM TransportationBrands trans")
    String findMaxCode();

    Page<TransportationBrands> findByTransportationBrandNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    TransportationBrands findByAgenciesId(int userId);
}