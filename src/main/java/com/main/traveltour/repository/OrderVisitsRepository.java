package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.OrderVisits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderVisitsRepository extends JpaRepository<OrderVisits, Integer> {

    @Query("SELECT MAX(ov.id) FROM OrderVisits ov")
    String findMaxCode();

    OrderVisits findById(String id);

    @Query("SELECT ov FROM OrderVisits ov " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "WHERE vl.id = :brandId AND ov.orderStatus = 0")
    Page<OrderVisits> findAllOrderVisits(@Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT ov FROM OrderVisits ov " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "WHERE LOWER(ov.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerCitizenCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ov.customerEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND vl.id = :brandId AND ov.orderStatus = 0")
    Page<OrderVisits> findAllOrderVisitsWithSearch(@Param("brandId") String brandId, @Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ov FROM OrderVisits ov " +
            "JOIN ov.tourDetails td " +
            "JOIN ov.orderVisitDetailsById ovd " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(vl.id = :visitId) AND " +
            "(ov.orderStatus = :orderVisitStatus) " +
            "GROUP BY ov.id")
    List<OrderVisits> findOrderVisitByTourDetailIdAndVisitId(@Param("tourDetailId") String tourDetailId,
                                                             @Param("visitId") String visitId,
                                                             @Param("orderVisitStatus") Integer orderVisitStatus);
}