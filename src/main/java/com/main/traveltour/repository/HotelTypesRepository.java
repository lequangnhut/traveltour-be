package com.main.traveltour.repository;

import com.main.traveltour.entity.HotelTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelTypesRepository extends JpaRepository<HotelTypes, Integer> {
}