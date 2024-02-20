package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Override
    public String findMaxId() {
        return roomTypesRepository.findMaxId();
    }

    @Override
    public Page<RoomTypes> findAll(Pageable pageable) {
        return roomTypesRepository.findAll(pageable);
    }

    @Override
    public Page<RoomTypes> findAllWithSearchAndHotelId(String searchTerm, String hotelId, Pageable pageable) {
        return roomTypesRepository.findBySearchTermAndHotelId(searchTerm, hotelId, pageable);
    }

    @Override
    public Page<RoomTypes> findAllByHotelIdAndIsDelete(String hotelId, Boolean isDelete, Pageable pageable) {
        return roomTypesRepository.findByHotelIdAndIsDeleted(hotelId, isDelete, pageable);
    }

    @Override
    public List<RoomTypes> findAllByHotelId(String hotelId) {
        return roomTypesRepository.findAllByHotelId(hotelId);
    }

    @Override
    public RoomTypes save(RoomTypes roomTypes) {
        return roomTypesRepository.save(roomTypes);
    }

    @Override
    public Optional<RoomTypes> findRoomTypeById(String roomTypeId) {
        return roomTypesRepository.findById(roomTypeId);
    }

    @Override
    public Optional<RoomTypes> findRoomTypeByRoomTypeId(String roomTypeId) {
        return roomTypesRepository.findById(roomTypeId);
    }

}
