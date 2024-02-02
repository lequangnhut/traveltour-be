package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.repository.RoomImagesRepository;
import com.main.traveltour.service.agent.RoomImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomImageServiceImpl implements RoomImageService {

    @Autowired
    RoomImagesRepository roomImagesRepository;

    @Override
    public RoomImages save(RoomImages roomImages) {
        return roomImagesRepository.save(roomImages);
    }

    @Override
    public List<RoomImages> getAllRoomsImagesByRoomId(String roomId) {
        return roomImagesRepository.findAllByRoomTypeId(roomId);
    }

    @Override
    public void deleteAllByIds(List<Integer> ids) {
        roomImagesRepository.deleteAllByIdIn(ids);
    }
}
