package com.main.traveltour.service.staff;

import com.main.traveltour.dto.staff.HotelsDto;
import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface HotelServiceService {

    Page<HotelsDto> findAvailableHotelsWithFilters(String searchTerm, String location, Date departureDate,
                                                   Date arrivalDate, Integer numAdults,
                                                   Integer numChildren, Integer numRooms, Pageable pageable);

    Hotels getHotelsById(String id);

    Hotels findByRoomTypeId(String roomTypeId);

    Long countHotels();

}
