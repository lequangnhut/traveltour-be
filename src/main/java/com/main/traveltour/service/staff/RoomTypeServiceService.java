package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoomTypeServiceService {

    Page<RoomTypes> findByHotelIdAndIsDeletedIsFalse(String hotelId, Pageable pageable);

    Page<RoomTypes> findByHotelIdWithUtilitiesAndSearchTerm(String searchTerm, String hotelId, Pageable pageable);
}
