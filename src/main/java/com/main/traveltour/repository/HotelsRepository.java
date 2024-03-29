package com.main.traveltour.repository;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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
            "WHERE (h.isAccepted = TRUE AND h.isActive = TRUE AND h.isDeleted = FALSE) AND " +
            "((:searchTerm IS NULL) OR (UPPER(h.hotelName) LIKE %:searchTerm% OR " +
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
            "AND ((oh.checkIn BETWEEN :checkIn AND :checkOut) " +
            "OR (oh.checkOut BETWEEN :checkIn AND :checkOut)) " +
            "AND oh.orderStatus <> 4")
    Integer calculateBookedRoomsByHotelId(@Param("hotelId") String hotelId,
                                          @Param("checkIn") Date checkIn,
                                          @Param("checkOut") Date checkOut);


    @Query("SELECT SUM(r.amountRoom) FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "WHERE h.id = :hotelId")
    Integer calculateAllHotelRoomNumbersByHotelId(@Param("hotelId") String hotelId);

    @Query("SELECT AVG(r.price) FROM Hotels h " +
            "JOIN h.roomTypesById r " +
            "WHERE h.id = :hotelId")
    Integer CalculateAverageHotelRoomPrice(@Param("hotelId") String hotelId);

    //fill khách sạn theo tour

    @Query("SELECT h FROM Hotels h " +
            "JOIN h.roomTypesById rt " +
            "JOIN rt.orderHotelDetailsById ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "JOIN oh.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(oh.orderStatus = :orderHotelStatus) AND " +
            "(:searchTerm IS NULL OR (UPPER(h.hotelName) LIKE %:searchTerm% OR " +
            "UPPER(h.province) LIKE %:searchTerm% OR " +
            "UPPER(h.district) LIKE %:searchTerm% OR " +
            "UPPER(h.ward) LIKE %:searchTerm% OR " +
            "UPPER(h.address) LIKE %:searchTerm% OR " +
            "UPPER(h.phone) LIKE %:searchTerm% OR " +
            "CAST(h.floorNumber AS string) LIKE %:searchTerm%)) " +
            "GROUP BY h.id")
    Page<Hotels> findHotelByTourDetailId(@Param("tourDetailId") String tourDetailId,
                                         @Param("orderHotelStatus") Integer orderHotelStatus,
                                         @Param("searchTerm") String searchTerm,
                                         Pageable pageable);

    @Query("SELECT h FROM Hotels h JOIN h.roomTypesById rt JOIN rt.orderHotelDetailsById ohd" +
            " JOIN ohd.orderHotelsByOrderHotelId oh WHERE (oh.userId = :userId) AND" +
            "(oh.orderStatus = :orderHotelStatus) ")
    Page<Hotels> findHotelByUserId(@Param("userId") Integer userId,
                                   @Param("orderHotelStatus") Integer orderHotelStatus,
                                   Pageable pageable);

    @Query("SELECT h FROM Hotels h JOIN h.roomTypesById rt WHERE rt.id = :roomTypeId")
    Hotels findByRoomTypeId(@Param("roomTypeId") String roomTypeId);

    @Query("SELECT h FROM Hotels h join h.agenciesByAgenciesId ag" +
            " WHERE (h.isAccepted = :isAccepted) and " +
            "(h.isActive = true) and (h.isDeleted = false) " +
            "and (ag.isActive) = true and (ag.isAccepted) = 2")
    Page<Hotels> findAllHotelByAcceptedAndTrueActive(@Param("isAccepted") Boolean isAccepted,Pageable pageable);

    @Query("SELECT h FROM Hotels h  join h.agenciesByAgenciesId ag " +
            "WHERE (h.isAccepted = :isAccepted) and (h.isActive = true) " +
            "and (h.isDeleted = false) and LOWER(h.hotelName) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%')) and (ag.isActive) = true and (ag.isAccepted) = 2")
    Page<Hotels> findAllHotelByAcceptedAndTrueActiveByName(@Param("isAccepted") Boolean isAccepted,Pageable pageable, String searchTerm);

    @Query("SELECT COUNT(ht) FROM Hotels ht")
    Long countHotels ();
}