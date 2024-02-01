package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomBeds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomBedsRepository extends JpaRepository<RoomBeds, Integer> {

    List<RoomBeds> findAllByBedTypeId(int id);
    RoomBeds findByRoomTypeId(String bedTypeId);

}