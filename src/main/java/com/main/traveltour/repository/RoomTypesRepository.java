package com.main.traveltour.repository;

import com.main.traveltour.dto.staff.RoomTypeAvailabilityDto;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RoomTypesRepository extends JpaRepository<RoomTypes, String>, JpaSpecificationExecutor<RoomTypes> {

    @Query("SELECT MAX(rt.id) FROM RoomTypes rt")
    String findMaxId();

    @Query("SELECT r FROM RoomTypes r " +
            "JOIN r.roomUtilities ru " +
            "WHERE ru.id = :id")
    List<RoomTypes> findAllByRoomUtilities(@Param("id") int id);

    List<RoomTypes> findAllByHotelId(String hotelId);

    @Query("SELECT roomType FROM RoomTypes roomType " +
            "WHERE (CAST(roomType.id AS string) LIKE %:searchTerm% OR " +
            "CAST(roomType.amountRoom AS string) LIKE %:searchTerm% OR " +
            "CAST(roomType.capacityAdults AS string) LIKE %:searchTerm% OR " +
            "roomType.roomTypeName LIKE %:searchTerm%) " + // Thêm điều kiện tìm kiếm cho roomTypeName
            "AND roomType.hotelId = :hotelId " +
            "AND roomType.isDeleted = false")
    Page<RoomTypes> findBySearchTermAndHotelId(@Param("searchTerm") String searchTerm, @Param("hotelId") String hotelId, Pageable pageable);

    @Query("SELECT rt FROM RoomTypes rt " +
            "WHERE rt.hotelId = :hotelId AND rt.isDeleted = false AND (" +
            " rt.id LIKE %:searchTerm% OR " +
            " rt.roomTypeName LIKE %:searchTerm% OR " +
            " CAST(rt.capacityAdults AS string) LIKE %:searchTerm% OR " +
            " CAST(rt.capacityChildren AS string) LIKE %:searchTerm% OR " +
            " CAST(rt.price AS string) LIKE %:searchTerm% OR " +
            " EXISTS (SELECT rb FROM RoomBeds rb JOIN rb.bedTypesByBedTypeId bt " +
            " WHERE rb.roomTypesByRoomTypeId = rt AND bt.bedTypeName LIKE %:searchTerm%) OR " +
            " EXISTS (SELECT ru FROM RoomUtilities ru JOIN ru.roomTypes rtc " +
            " WHERE rtc = rt AND ru.roomUtilitiesName LIKE %:searchTerm%))")
    Page<RoomTypes> findByHotelIdWithUtilitiesAndSearchTerm(@Param("searchTerm") String searchTerm, @Param("hotelId") String hotelId, Pageable pageable);

    Page<RoomTypes> findByHotelIdAndIsDeletedIsFalse(String hotelId, Pageable pageable);


    Page<RoomTypes> findByHotelIdAndIsDeleted(String hotelId, Boolean isDeleted, Pageable pageable);

    @Query("SELECT MAX(ohd.amount) FROM OrderHotelDetails ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "WHERE ohd.roomTypesByRoomTypeId.id = :roomTypeId " +
            "AND (oh.checkIn BETWEEN :checkIn AND :checkOut " +
            "OR (oh.checkOut BETWEEN :checkIn AND :checkOut) " +
            "AND oh.checkOut != :checkIn) " +
            "AND oh.orderStatus <> 4")
    Integer calculateBookedRooms(@Param("roomTypeId") String roomTypeId,
                                 @Param("checkIn") Timestamp checkIn,
                                 @Param("checkOut") Timestamp checkOut);

    Optional<RoomTypes> findById(String s);

    List<RoomTypes> findByIdIn(List<String> ids);

    @Query("SELECT rt FROM RoomTypes rt join rt.hotelsByHotelId ht" +
            " WHERE (rt.isActive = :isActive) and " +
            "(rt.isDeleted = false) and (ht.id = :id)")
    Page<RoomTypes> findPostByHotelId(@Param("isActive") Integer isActive, @Param("id") String id, Pageable pageable);

    @Query("SELECT rt FROM RoomTypes rt join rt.hotelsByHotelId ht" +
            " WHERE (rt.isActive = :isActive) and " +
            "(rt.isDeleted = false) and (ht.id = :id) " +
            "and LOWER(rt.roomTypeName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<RoomTypes> findPostByHotelIdByName(@Param("isActive") Integer isActive, @Param("id") String id, Pageable pageable, String searchTerm);

    @Query("SELECT rt FROM RoomTypes rt WHERE rt.id = :id")
    RoomTypes findByRoomId(String id);
}