package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationTypesRepository extends JpaRepository<TransportationTypes, Integer> {

    TransportationTypes findById(int id);
}