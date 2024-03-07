package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransportationBrandsRepository extends JpaRepository<TransportationBrands, Integer> {

    @Query(value = "SELECT MAX(trans.id) FROM TransportationBrands trans")
    String findMaxCode();

    List<TransportationBrands> findAllByAgenciesIdAndIsActiveTrue(int agenciesId);

    TransportationBrands findByAgenciesId(int agenciesId);

    TransportationBrands findById(String transportBrandId);

    List<TransportationBrands> findAllByIsActiveIsTrueAndIsAcceptedIsTrue();

    @Query("SELECT br FROM TransportationBrands br " +
            "WHERE br.isAccepted = true AND br.isActive = true")
    Page<TransportationBrands> findAllCustomer(Pageable pageable);
}