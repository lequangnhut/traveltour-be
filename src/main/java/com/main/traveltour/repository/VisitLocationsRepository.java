package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitLocationsRepository extends JpaRepository<VisitLocations, Integer> {

    @Query(value = "SELECT MAX(pl.id) FROM VisitLocations pl")
    String findMaxCode();

    VisitLocations findByAgenciesId(int userId);

    VisitLocations findById(String visitLocationsId);

    List<VisitLocations> findAllByAgenciesIdAndIsActiveTrue(int userId);

    List<VisitLocations> findAllByVisitLocationTypeId(int id);

    List<VisitLocations> findAllById(String id);

    VisitLocations findByIdAndIsActiveIsTrue(String id);

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE vl.isActive = TRUE AND vl.isAccepted = TRUE " +
            "GROUP BY vl.id")
    Page<VisitLocations> getAllByIsActiveIsTrueAndIsAcceptedIsTrue(Pageable pageable);

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE UPPER(vl.visitLocationName) LIKE %:searchTerm% OR " +
            "UPPER(vl.phone) LIKE %:searchTerm% OR " +
            "UPPER(vl.province) LIKE %:searchTerm% OR " +
            "UPPER(vl.district) LIKE %:searchTerm% OR " +
            "UPPER(vl.ward) LIKE %:searchTerm% AND " +
            "UPPER(vlt.ticketTypeName) LIKE %:searchTerm% AND " +
            "UPPER(CAST(vlt.unitPrice AS string)) LIKE %:searchTerm% AND " +
            "vl.isActive = TRUE AND vl.isAccepted = TRUE")
    Page<VisitLocations> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (:location IS NULL OR UPPER(vl.province) LIKE CONCAT('%', UPPER(:location), '%'))AND " +
            "vl.isActive = TRUE AND vl.isAccepted = TRUE")
    Page<VisitLocations> findVisitLocationsByProvince(
            @Param("location") String location,
            Pageable pageable);

    //fill tham quan theo tour

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.orderVisitsById ov " +
            "JOIN ov.orderVisitDetailsById ovd " +
            "JOIN ov.tourDetails td " +
            "WHERE td.id = :tourDetailId AND " +
            "ov.orderStatus = :orderVisitStatus AND " +
            "(:searchTerm IS NULL OR (UPPER(vl.visitLocationName) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.province) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.district) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.ward) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.address) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.phone) LIKE CONCAT('%', UPPER(:searchTerm), '%'))) " +
            "GROUP BY vl.id")
    Page<VisitLocations> findVisitByTourDetailId(@Param("tourDetailId") String tourDetailId,
                                                     @Param("orderVisitStatus") Integer orderVisitStatus,
                                                 @Param("searchTerm") String searchTerm,
                                                 Pageable pageable);

}