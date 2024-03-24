package com.main.traveltour.service.admin;

import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomTypesServiceAD {

    List<RoomTypes> findByRoomUtilityTypeId(int typeId);

    Page<RoomTypes> findByHotelId(Integer isActive, String id, Pageable pageable);

    Page<RoomTypes> findByHotelIdAndName(Integer isActive, String id, Pageable pageable, String searchTerm);

    RoomTypes findById (String id);

    RoomTypes save (RoomTypes roomTypes);
}
