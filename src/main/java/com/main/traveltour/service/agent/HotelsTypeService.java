package com.main.traveltour.service.agent;

import com.main.traveltour.entity.HotelTypes;

import java.util.List;
import java.util.Optional;

public interface HotelsTypeService {

    List<HotelTypes> findAllHotelType();

    Optional<HotelTypes> findById(Integer id);
}
