package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.RoomUtilities;
import com.main.traveltour.repository.RoomUtilitiesRepository;
import com.main.traveltour.service.admin.RoomUilityServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomUtilityServiceImplAD implements RoomUilityServiceAD {

    @Autowired
    private RoomUtilitiesRepository roomUtilitiesRepository;

    @Override
    public Page<RoomUtilities> findAll(Pageable pageable) {
        return roomUtilitiesRepository.findAll(pageable);
    }

    @Override
    public Page<RoomUtilities> findAllWithSearch(String searchTerm, Pageable pageable) {
        return roomUtilitiesRepository.findByRoomUtilitiesNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public RoomUtilities findByRoomUtilityName(String name) {
        return roomUtilitiesRepository.findByRoomUtilitiesName(name);
    }

    @Override
    public RoomUtilities findById(int id) {
        return roomUtilitiesRepository.findById(id);
    }

//    @Override
//    public List<String> findRoomUtilitiesNameByRoomTypeId(String roomTypeId) {
//        return roomUtilitiesRepository.findRoomUtilitiesNameByRoomTypeId(roomTypeId);
//    }

    @Override
    public RoomUtilities save(RoomUtilities roomUtilities) {
        return roomUtilitiesRepository.save(roomUtilities);
    }

    @Override
    public RoomUtilities delete(int id) {
        roomUtilitiesRepository.deleteById(id);
        return null;
    }
}
