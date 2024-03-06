package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
            "JOIN h.roomTypesById r " +
            "WHERE ((:searchTerm IS NULL) OR (UPPER(h.hotelName) LIKE %:searchTerm% OR " +
            "UPPER(h.phone) LIKE %:searchTerm% OR " +
            "UPPER(h.province) LIKE %:searchTerm% OR " +
            "UPPER(h.district) LIKE %:searchTerm% OR " +
            "UPPER(h.ward) LIKE %:searchTerm% OR " +
            "CAST(r.price AS string) LIKE %:searchTerm%)) AND " +
            "(:location IS NULL OR UPPER(h.province)LIKE CONCAT('%', UPPER(:location),'%')) AND " +
            "(:numAdults IS NULL OR r.capacityAdults >= :numAdults) AND " +
            "(:numChildren IS NULL OR r.capacityChildren >= :numChildren)" +
            "GROUP BY h")
    List<Hotels> findAllBySearch(@Param("searchTerm") String searchTerm,
                                 @Param("location") String location,
                                 @Param("numAdults") Integer numAdults,
                                 @Param("numChildren") Integer numChildren
                                  );

    @Query("SELECT SUM(ohd.amount) FROM OrderHotelDetails ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "WHERE ohd.roomTypesByRoomTypeId.hotelId = :hotelId " +
            "AND ((oh.checkIn <= :checkOut AND oh.checkOut > :checkOut) OR  " +
            "(oh.checkIn < :checkIn AND oh.checkOut >= :checkIn) OR  " +
            "(oh.checkIn >= :checkOut AND oh.checkOut <= :checkIn)) " +
            "AND oh.orderStatus <> 4")
    Integer calculateBookedRoomsByHotelId(@Param("hotelId") String hotelId,
                                          @Param("checkIn") Timestamp checkIn,
                                          @Param("checkOut") Timestamp checkOut);

    @Query("SELECT SUM(r.amountRoom) FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "WHERE h.id = :hotelId")
    Integer calculateAllHotelRoomNumbersByHotelId(@Param("hotelId") String hotelId);

    @Query("SELECT AVG(r.price) FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "WHERE h.id = :hotelId")
    Integer CalculateAverageHotelRoomPrice(@Param("hotelId") String hotelId);


}