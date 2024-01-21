package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HotelsRepository extends JpaRepository<Hotels, Integer> {

    @Query(value = "SELECT MAX(hl.id) FROM Hotels hl")
    String findMaxCode();

    Hotels findByAgenciesId(int agencyId);
}