package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.HotelsDto;
import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.sql.Timestamp;

public interface HotelServiceService {

    Page<HotelsDto> findAvailableHotelsWithFilters(String searchTerm, String location, Timestamp departureDate,
                                                   Timestamp arrivalDate, Integer numAdults,
                                                   Integer numChildren, Integer numRooms, Pageable pageable);

    Hotels getHotelsById(String id);


}
