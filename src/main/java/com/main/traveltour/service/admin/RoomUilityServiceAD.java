package com.main.traveltour.service.admin;

import com.main.traveltour.entity.RoomUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomUilityServiceAD {

    Page<RoomUtilities> findAll(Pageable pageable);

    Page<RoomUtilities> findAllWithSearch(String searchTerm, Pageable pageable);

    RoomUtilities findByRoomUtilityName(String name);

    RoomUtilities findById(int id);

    RoomUtilities save(RoomUtilities roomUtilities);

    RoomUtilities delete(int id);

}
