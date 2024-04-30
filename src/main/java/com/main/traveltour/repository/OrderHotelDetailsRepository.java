package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderHotelDetailsRepository extends JpaRepository<OrderHotelDetails, Integer> {
    @Query("SELECT ohd FROM Hotels h " +
            "JOIN h.roomTypesById rt " +
            "JOIN rt.orderHotelDetailsById ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "JOIN oh.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(h.id = :hotelId)")
    List<OrderHotelDetails> findOrderHotelDetailByTourDetailIdAndHotelId(@Param("tourDetailId") String tourDetailId,
                                                                         @Param("hotelId") String hotelId);

    @Query("SELECT ohd FROM OrderHotelDetails ohd join ohd.roomTypesByRoomTypeId rt where ohd.orderHotelId = :orderHotelId")
    List<OrderHotelDetails> findOrderHotelDetailByOrderHotelId(@Param("orderHotelId") String orderHotelId);

    OrderHotelDetails findByRoomTypeId(String roomTypeId);
}