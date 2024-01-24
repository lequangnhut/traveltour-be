package com.main.traveltour.service.admin;

import com.main.traveltour.entity.HotelTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelTypesServiceAD {

    Page<HotelTypes> findAll(Pageable pageable);

    Page<HotelTypes> findAllWithSearch(String searchTerm, Pageable pageable);

    HotelTypes findByHotelTypeName(String name);

    HotelTypes findById(int id);

    HotelTypes save(HotelTypes hotelTypes);

    HotelTypes delete(int id);
}
