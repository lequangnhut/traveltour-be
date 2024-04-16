package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImagesRepository extends JpaRepository<RoomImages, Integer> {
    List<RoomImages> findAllByRoomTypeId(String roomTypeId);

    void deleteAllByIdIn(List<Integer> ids);

}