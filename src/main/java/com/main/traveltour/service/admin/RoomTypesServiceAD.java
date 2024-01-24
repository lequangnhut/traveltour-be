package com.main.traveltour.service.admin;

import com.main.traveltour.entity.RoomTypes;

import java.util.List;

public interface RoomTypesServiceAD {

    List<RoomTypes> findByRoomUtilityTypeId(int typeId);

}
