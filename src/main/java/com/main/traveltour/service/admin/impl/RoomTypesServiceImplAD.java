package com.main.traveltour.service.admin.impl;


import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.admin.RoomTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypesServiceImplAD implements RoomTypesServiceAD {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Override
    public List<RoomTypes> findByRoomUtilityTypeId(int typeId) {
        return roomTypesRepository.findAllByRoomUtilities(typeId);
    }
}
