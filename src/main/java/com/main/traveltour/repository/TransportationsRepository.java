package com.main.traveltour.repository;

import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportationsRepository extends JpaRepository<Transportations, Integer> {
    @Query("SELECT MAX(t.id) FROM Transportations t")
    String fixMaxCode();

    Transportations findById(String id);

    Transportations findByLicensePlate(String licensePlate);

    @Query("SELECT t FROM Transportations t " +
            "WHERE t.transportationBrandsByTransportationBrandId.id = :brandId")
    Page<Transportations> findAllTransport(@Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT t FROM Transportations t " +
            "WHERE LOWER(CAST(t.amountSeat AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.licensePlate) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND t.transportationBrandsByTransportationBrandId.id = :brandId")
    Page<Transportations> findByTransportWithSearch(@Param("brandId") String brandId, @Param("searchTerm") String searchTerm, Pageable pageable);

    List<Transportations> findAllByTransportationTypeId(int id);

}