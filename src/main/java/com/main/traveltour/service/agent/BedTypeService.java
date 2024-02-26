package com.main.traveltour.service.agent;

import com.main.traveltour.entity.BedTypes;
import com.main.traveltour.entity.RoomBeds;

import java.util.List;

public interface BedTypeService {

    List<BedTypes> findAllListBedTypes();

}
