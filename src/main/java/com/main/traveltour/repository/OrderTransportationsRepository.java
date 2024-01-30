package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTransportationsRepository extends JpaRepository<OrderTransportations, Integer> {

    @Query("SELECT MAX(ort.id) FROM OrderTransportations ort")
    String findMaxCode();

    OrderTransportations findById(String id);

    @Query("SELECT ort FROM OrderTransportations ort " +
            "JOIN ort.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "JOIN t.transportationBrandsByTransportationBrandId tbr " +
            "WHERE tbr.id = :transportBrandId AND ort.orderStatus = 0")
    Page<OrderTransportations> findAllOrderTransport(@Param("transportBrandId") String transportBrandId, Pageable pageable);

    @Query("SELECT ort FROM OrderTransportations ort " +
            "JOIN ort.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "JOIN t.transportationBrandsByTransportationBrandId tbr " +
            "WHERE LOWER(ort.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.transportationScheduleId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerCitizenCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(CAST(ort.dateCreated AS STRING) ) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND t.id = :transportBrandId AND ort.orderStatus = 0")
    Page<OrderTransportations> findAllOrderTransportWithSearch(@Param("transportBrandId") String transportBrandId, @Param("searchTerm") String searchTerm, Pageable pageable);
}