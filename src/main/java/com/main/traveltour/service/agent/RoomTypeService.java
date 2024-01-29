package com.main.traveltour.service.agent;

import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {

    String findMaxId();
    Page<RoomTypes> findAll(Pageable pageable);

    Page<RoomTypes> findAllByHotelIdAndIsDelete(String hotelId, Boolean isDelete, Pageable pageable);
    Page<RoomTypes> findAllWithSearchAndHotelId(String searchTerm, String hotelId, Pageable pageable);
    List<RoomTypes> findAllByHotelId(String hotelId);
    RoomTypes save(RoomTypes roomTypes);
    Optional<RoomTypes> findRoomTypeById(String roomTypeId);
}
