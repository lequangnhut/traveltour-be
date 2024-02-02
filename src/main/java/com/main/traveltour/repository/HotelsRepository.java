package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface HotelsRepository extends JpaRepository<Hotels, String> {

    @Query(value = "SELECT MAX(hl.id) FROM Hotels hl")
    String findMaxCode();

    List<Hotels> findAllByAgenciesId(int agencyId);

    Hotels findByAgenciesId(int agencyId);

    List<Hotels> findByHotelTypeId(int id);

    @Query("SELECT h FROM Hotels h " +
            "JOIN h.placeUtilities pu " +
            "WHERE pu.id = :id")
    List<Hotels> findAllByPlaceUtilities(@Param("id") int id);

    /*hotel service*/

    Hotels getHotelsById(String id);

    @Query("SELECT h FROM Hotels h " +
            "JOIN h.roomTypesById")
    Page<Hotels> findAllHotel(Pageable pageable);

    @Query("SELECT h FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "WHERE UPPER(h.hotelName) LIKE %:searchTerm% OR " +
            "UPPER(h.phone) LIKE %:searchTerm% OR " +
            "UPPER(h.province) LIKE %:searchTerm% OR " +
            "UPPER(h.district) LIKE %:searchTerm% OR " +
            "UPPER(h.ward) LIKE %:searchTerm% OR " +
            "CAST(r.price AS string) LIKE %:searchTerm%")
    Page<Hotels> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT h FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "JOIN h.agenciesByAgenciesId a " +
            "JOIN a.usersByUserId u " +
            "JOIN u.tourDetailsById td " +
            "WHERE (:location IS NULL OR UPPER(h.province) LIKE UPPER(CONCAT('%', :location, '%'))) AND " +
            "(:departureDate IS NULL OR td.departureDate <= :departureDate) AND " +
            "(:arrivalDate IS NULL OR td.arrivalDate >= :arrivalDate) AND " +
            "(:numAdults IS NULL OR r.capacityAdults >= :numAdults) AND " +
            "(:numChildren IS NULL OR r.capacityChildren >= :numChildren)")
    Page<Hotels> findHotelsWithFilters(
            @Param("location") String location,
            @Param("departureDate") Date departureDate,
            @Param("arrivalDate") Date arrivalDate,
            @Param("numAdults") Integer numAdults,
            @Param("numChildren") Integer numChildren,
            Pageable pageable);

}