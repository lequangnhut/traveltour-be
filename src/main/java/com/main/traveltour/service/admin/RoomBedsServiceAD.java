package com.main.traveltour.service.admin;

import com.main.traveltour.entity.RoomBeds;

import java.util.List;

public interface RoomBedsServiceAD {
    List<RoomBeds> findByBedTypeId(int typeId);
    void save(RoomBeds roomBeds);
}
