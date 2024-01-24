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
}
