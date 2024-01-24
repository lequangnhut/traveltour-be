package com.main.traveltour.repository;

import com.main.traveltour.entity.TourTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourTypesRepository extends JpaRepository<TourTypes, Integer> {

    Page<TourTypes> findByTourTypeNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    TourTypes findByTourTypeName(String name);

    TourTypes findAllById(int id);

    Optional<TourTypes> findById(int id);

    void deleteById(int id);

}