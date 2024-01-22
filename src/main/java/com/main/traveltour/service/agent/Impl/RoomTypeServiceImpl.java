package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Override
    public String findMaxId() {
        return roomTypesRepository.findMaxId();
    }

    @Override
    public RoomTypes save(RoomTypes roomTypes) {
        return roomTypesRepository.save(roomTypes);
    }
}
