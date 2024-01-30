package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitLocationsRepository extends JpaRepository<VisitLocations, Integer> {

    @Query(value = "SELECT MAX(pl.id) FROM VisitLocations pl")
    String findMaxCode();

    VisitLocations findByAgenciesId(int userId);

    VisitLocations findById(String visitLocationsId);

    List<VisitLocations> findAllByAgenciesIdAndIsActiveTrue(int userId);

    List<VisitLocations> findAllByVisitLocationTypeId(int id);
}