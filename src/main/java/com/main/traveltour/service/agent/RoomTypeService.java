package com.main.traveltour.service.agent;

import com.main.traveltour.entity.RoomTypes;

public interface RoomTypeService {

    String findMaxId();

    RoomTypes save(RoomTypes roomTypes);
}
