package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationTypesRepository extends JpaRepository<TransportationTypes, Integer> {

    Page<TransportationTypes> findByTransportationTypeNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    TransportationTypes findById(int id);

    TransportationTypes findByTransportationTypeName(String name);

    void deleteById(int id);

}