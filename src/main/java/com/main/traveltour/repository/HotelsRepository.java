package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelsRepository extends JpaRepository<Hotels, Integer> {

    Hotels findByAgenciesId(int agencyId);
}