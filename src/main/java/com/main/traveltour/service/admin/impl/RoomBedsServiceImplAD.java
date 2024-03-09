package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.RoomBeds;
import com.main.traveltour.repository.RoomBedsRepository;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBedsServiceImplAD implements RoomBedsServiceAD {

    @Autowired
    private RoomBedsRepository roomBedsRepository;


    @Override
    public List<RoomBeds> findByBedTypeId(int typeId) {
        return roomBedsRepository.findAllByBedTypeId(typeId);
    }

    @Override
    public RoomBeds findRoomBedsRoomTypeId(String roomBedsId) {
        return roomBedsRepository.findByRoomTypeId(roomBedsId);
    }

    @Override
    public void save(RoomBeds roomBeds) {
        roomBedsRepository.save(roomBeds);
    }

    @Override
    public List<RoomBeds> findAllRoomBedType() {
        return roomBedsRepository.findAll();
    }

    @Override
    public RoomBeds findById(int id) {
        return roomBedsRepository.findById(id).get();
    }
}
