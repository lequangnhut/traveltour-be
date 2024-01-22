package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomUtilities;
import com.main.traveltour.repository.RoomUtilitiesRepository;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomUtilitiesServiceImpl implements RoomUtilitiesService {

    @Autowired
    private RoomUtilitiesRepository roomUtilitiesRepository;

    @Override
    public RoomUtilities findByRoomUtilitiesId(int id) {
        return roomUtilitiesRepository.findById(id);
    }

    @Override
    public List<RoomUtilities> findAllRoomUtils() {
        return roomUtilitiesRepository.findAll();
    }
}
