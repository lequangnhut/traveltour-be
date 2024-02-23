package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
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


    @Query(value = "SELECT h.*  " +
            "FROM hotels h  " +
            "         JOIN room_types r ON h.id = r.hotel_id  " +
            "WHERE (:location IS NULL OR UPPER(h.province) LIKE CONCAT('%', UPPER(:location), '%'))  " +
            "  AND (:numAdults IS NULL OR r.capacity_adults >= :numAdults)  " +
            "  AND (:numChildren IS NULL OR r.capacity_children >= :numChildren)  " +
            "GROUP BY r.hotel_id  " +
            "             having  (:numRooms IS NULL OR :numRooms <= (sum(r.amount_room) - (  " +
            "    SELECT COALESCE(SUM(ohd.amount), 0) as nu  " +
            "    FROM hotels h  " +
            "             JOIN room_types r ON h.id = r.hotel_id  " +
            "        JOIN order_hotel_details ohd  " +
            "             JOIN order_hotels oh ON ohd.order_hotel_id = oh.id  " +
            "    WHERE ohd.room_type_id = r.id  " +
            "      AND ((oh.check_in <= :arrivalDate AND oh.check_out > :arrivalDate) OR  " +
            "           (oh.check_in < :departureDate AND oh.check_out >= :departureDate) OR  " +
            "           (oh.check_in >= :arrivalDate AND oh.check_out <= :departureDate))  " +
            ")))", nativeQuery = true)
    Page<Hotels> findAvailableHotelsWithFilters(
            @Param("location") String location,
            @Param("departureDate") Timestamp departureDate,
            @Param("arrivalDate") Timestamp arrivalDate,
            @Param("numAdults") Integer numAdults,
            @Param("numChildren") Integer numChildren,
            @Param("numRooms") Integer numRooms,
            Pageable pageable);

}