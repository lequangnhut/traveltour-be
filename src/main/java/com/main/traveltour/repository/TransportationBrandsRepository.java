package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportationBrandsRepository extends JpaRepository<TransportationBrands, Integer> {

    TransportationBrands findByUserId(int userId);
}