package com.main.traveltour.service.admin.impl;


import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.admin.RoomTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypesServiceImplAD implements RoomTypesServiceAD {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Override
    public List<RoomTypes> findByRoomUtilityTypeId(int typeId) {
        return roomTypesRepository.findAllByRoomUtilities(typeId);
    }

    @Override
    public Page<RoomTypes> findByHotelId(Integer isActive, String id, Pageable pageable) {
        return roomTypesRepository.findPostByHotelId(isActive, id, pageable);
    }

    @Override
    public Page<RoomTypes> findByHotelIdAndName(Integer isActive, String id, Pageable pageable, String searchTerm) {
        return roomTypesRepository.findPostByHotelIdByName(isActive, id, pageable, searchTerm);
    }

    @Override
    public RoomTypes findById(String id) {
        return roomTypesRepository.findByRoomId(id);
    }

    @Override
    public RoomTypes save(RoomTypes roomTypes) {
        return roomTypesRepository.save(roomTypes);
    }
}
