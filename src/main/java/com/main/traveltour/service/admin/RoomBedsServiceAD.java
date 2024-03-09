package com.main.traveltour.service.admin;

import com.main.traveltour.entity.RoomBeds;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface RoomBedsServiceAD {
    List<RoomBeds> findByBedTypeId(int typeId);
    RoomBeds findRoomBedsRoomTypeId(String roomBedsId);
    void save(RoomBeds roomBeds);

    List<RoomBeds> findAllRoomBedType();

    RoomBeds findById(int id);
}
