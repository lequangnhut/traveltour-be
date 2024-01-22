package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.repository.RoomImagesRepository;
import com.main.traveltour.service.agent.RoomImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomImageServiceImpl implements RoomImageService {

    @Autowired
    RoomImagesRepository roomImagesRepository;

    @Override
    public RoomImages save(RoomImages roomImages) {
        return roomImagesRepository.save(roomImages);
    }
}
