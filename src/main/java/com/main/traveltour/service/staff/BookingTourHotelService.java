package com.main.traveltour.service.staff;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BookingTourHotelService {

    Page<Hotels> findHotelByTourDetailId(String tourDetailId, Integer orderHotelStatus, String searchTerm, Pageable pageable);

    List<OrderHotelDetails> findOrderHotelDetailByTourDetailIdAndHotelId(String tourDetailId, String hotelId);

    List<OrderHotels> findOrderHotelByTourDetailIdAndHotelId(String tourDetailId, String hotelId);

    void update(OrderHotels orderHotels);

    Page<Hotels> findHotelByUserId(Integer userId, Integer orderHotelStatus, Pageable pageable);

    Page<Hotels> findHotelByTourDetailIdForGuide(String tourDetailId, Pageable pageable, String searchTerm);


}
