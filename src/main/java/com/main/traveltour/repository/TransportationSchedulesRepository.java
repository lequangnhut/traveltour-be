package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationSchedulesRepository extends JpaRepository<TransportationSchedules, Integer> {

    @Query("SELECT MAX(t.id) FROM TransportationSchedules t")
    String fixMaxCode();

    TransportationSchedules findById(String id);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId")
    Page<TransportationSchedules> findAllSchedules(@Param("transportBrandId") String transportBrandId, Pageable pageable);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE LOWER(CAST(t.unitPrice AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND tpb.id = :transportBrandId")
    Page<TransportationSchedules> findAllSchedulesWithSearch(@Param("transportBrandId") String transportBrandId, @Param("searchTerm") String searchTerm, Pageable pageable);
}