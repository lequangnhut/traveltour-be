package com.main.traveltour.repository;

import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationsRepository extends JpaRepository<Transportations, Integer> {

    @Query("SELECT MAX(t.id) FROM Transportations t")
    String fixMaxCode();

    Transportations findById(String id);

    @Query("SELECT t FROM Transportations t " +
            "WHERE LOWER(CAST(t.amountSeat AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.licensePlate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Transportations> findByTransportWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}