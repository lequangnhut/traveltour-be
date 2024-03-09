package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderHotelsRepository extends JpaRepository<OrderHotels, Integer> {
    @Query("SELECT COALESCE(MAX(oh.id), 'OH0000') FROM OrderHotels oh")
    String maxCodeTourId();

    @Query("SELECT oh FROM Hotels h " +
            "JOIN h.roomTypesById rt " +
            "JOIN rt.orderHotelDetailsById ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "JOIN oh.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(h.id = :hotelId) " +
            "GROUP BY oh.id")
    List<OrderHotels> findOrderHotelByTourDetailIdAndHotelId(@Param("tourDetailId") String tourDetailId,
                                                             @Param("hotelId") String hotelId);

}