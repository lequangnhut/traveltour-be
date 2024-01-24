package com.main.traveltour.service.agent;

import com.main.traveltour.entity.RoomUtilities;

import java.util.List;

public interface RoomUtilitiesService {

    RoomUtilities findByRoomUtilitiesId(int id);

    List<RoomUtilities> findAllRoomUtils();


}
