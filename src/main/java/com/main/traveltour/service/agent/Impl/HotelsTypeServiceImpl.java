package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.repository.HotelTypesRepository;
import com.main.traveltour.service.agent.HotelsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelsTypeServiceImpl implements HotelsTypeService {

    @Autowired
    private HotelTypesRepository hotelTypesRepository;

    @Override
    public List<HotelTypes> findAllHotelType() {
        return hotelTypesRepository.findAll();
    }
}
