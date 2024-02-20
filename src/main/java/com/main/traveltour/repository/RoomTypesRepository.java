package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Page<RoomTypes> findByHotelIdAndIsDeleted(String hotelId, Boolean isDelete, Pageable pageable);

    Optional<RoomTypes> findById(String s);
}