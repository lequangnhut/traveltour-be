package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.RoomTypeAvailabilityDto;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;


public interface RoomTypeServiceService {

    Page<RoomTypeAvailabilityDto> findByHotelIdWithUtilitiesAndSearchTerm(String searchTerm, String hotelId, Timestamp checkIn, Timestamp checkOut, Pageable pageable);

    Page<RoomTypeAvailabilityDto> findRoomAvailabilityByHotelIdAndDateRange(String hotelId, Timestamp checkIn, Timestamp checkOut, Pageable pageable);

}
