package com.main.traveltour.service.staff;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;

public interface HotelServiceService {
    Page<Hotels> findAllHotel(Pageable pageable);

    Page<Hotels> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<Hotels> findHotelsWithFilters(String location, Date departureDate,
                                                Date arrivalDate, Integer numAdults,
                                                Integer numChildren, Pageable pageable);

    Hotels getHotelsById(String id);


}
