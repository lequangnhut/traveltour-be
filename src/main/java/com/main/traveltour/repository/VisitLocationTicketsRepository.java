package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocationTickets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitLocationTicketsRepository extends JpaRepository<VisitLocationTickets, Integer> {

    @Query("SELECT MAX(tk.id) FROM VisitLocationTickets tk")
    String findMaxCode();

    VisitLocationTickets findById(int visitTicketId);

    VisitLocationTickets findByTicketTypeNameAndVisitLocationId(String ticketTypeName, String locationId);

    List<VisitLocationTickets> findByVisitLocationId(String visitLocationId);

    @Query("SELECT tkt FROM VisitLocationTickets tkt " +
            "JOIN tkt.visitLocationsByVisitLocationId vil " +
            "WHERE vil.id = :brandId")
    Page<VisitLocationTickets> findAllVisitTickets(@Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT tkt FROM VisitLocationTickets tkt " +
            "JOIN tkt.visitLocationsByVisitLocationId vil " +
            "WHERE LOWER(tkt.ticketTypeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(CAST(tkt.unitPrice AS STRING) ) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vil.visitLocationName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vil.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vil.province) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vil.district) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vil.ward) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND vil.id = :brandId")
    Page<VisitLocationTickets> findAllWithSearchVisitTickets(@Param("brandId") String brandId, String searchTerm, Pageable pageable);
}