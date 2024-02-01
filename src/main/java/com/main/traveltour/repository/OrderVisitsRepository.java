package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderVisits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderVisitsRepository extends JpaRepository<OrderVisits, Integer> {

    @Query("SELECT MAX(ov.id) FROM OrderVisits ov")
    String findMaxCode();

    OrderVisits findById(String id);

    @Query("SELECT ov FROM OrderVisits ov " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "WHERE vl.id = :brandId")
    Page<OrderVisits> findAllOrderVisits(@Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT ov FROM OrderVisits ov " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "WHERE LOWER(ov.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerCitizenCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND vl.id = :brandId")
    Page<OrderVisits> findAllOrderVisitsWithSearch(@Param("brandId") String brandId, @Param("searchTerm") String searchTerm, Pageable pageable);
}