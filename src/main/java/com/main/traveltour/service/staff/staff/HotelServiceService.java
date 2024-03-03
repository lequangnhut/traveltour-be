package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.sql.Timestamp;

public interface HotelServiceService {
    Page<Hotels> findAllHotel(Pageable pageable);

    Page<Hotels> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<Hotels> findAvailableHotelsWithFilters(String location, Timestamp departureDate,
                                                Timestamp arrivalDate, Integer numAdults,
                                                Integer numChildren, Integer numRooms, Pageable pageable);

    Hotels getHotelsById(String id);


}
