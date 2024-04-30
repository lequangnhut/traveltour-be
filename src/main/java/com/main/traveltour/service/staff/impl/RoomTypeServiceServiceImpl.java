package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.staff.RoomTypeAvailabilityDto;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.staff.RoomTypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceServiceImpl implements RoomTypeServiceService {

    @Autowired
    private RoomTypesRepository repo;

    @Override
    public Page<RoomTypeAvailabilityDto> findByHotelIdWithUtilitiesAndSearchTerm(String searchTerm, String hotelId, Timestamp checkIn, Timestamp checkOut, Pageable pageable) {
        Page<RoomTypes> roomTypesPage = repo.findByHotelIdWithUtilitiesAndSearchTerm(searchTerm, hotelId, pageable);

        List<RoomTypeAvailabilityDto> dtos = roomTypesPage.getContent().stream().map(roomType -> {
            int bookedRooms = calculateBookedRooms(roomType.getId(), checkIn, checkOut);
            RoomTypeAvailabilityDto dto = new RoomTypeAvailabilityDto(
                    roomType.getId(),
                    roomType.getRoomTypeName(),
                    roomType.getAmountRoom(),
                    bookedRooms
            );

            dto.setHotelId(roomType.getHotelId());
            dto.setCapacityAdults(roomType.getCapacityAdults());
            dto.setCapacityChildren(roomType.getCapacityChildren());
            dto.setPrice(roomType.getPrice());
            dto.setBreakfastIncluded(roomType.getBreakfastIncluded());
            dto.setFreeCancellation(roomType.getFreeCancellation());
            dto.setCheckinTime(roomType.getCheckinTime());
            dto.setCheckoutTime(roomType.getCheckoutTime());
            dto.setIsActive(roomType.getIsActive());
            dto.setIsDeleted(roomType.getIsDeleted());
            dto.setDateDeleted(roomType.getDateDeleted());
            dto.setRoomTypeAvatar(roomType.getRoomTypeAvatar());
            dto.setRoomTypeDescription(roomType.getRoomTypeDescription());
            dto.setRoomUtilities(new ArrayList<>(roomType.getRoomUtilities()));

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, roomTypesPage.getTotalElements());
    }
    @Override
    public Page<RoomTypeAvailabilityDto> findRoomAvailabilityByHotelIdAndDateRange(String hotelId, Timestamp checkIn, Timestamp checkOut, Pageable pageable) {
        Page<RoomTypes> roomTypesPage = repo.findByHotelIdAndIsDeletedIsFalse(hotelId, pageable);

        List<RoomTypeAvailabilityDto> dtos = roomTypesPage.getContent().stream().map(roomType -> {
            int bookedRooms = calculateBookedRooms(roomType.getId(), checkIn, checkOut);
            RoomTypeAvailabilityDto dto = new RoomTypeAvailabilityDto(
                    roomType.getId(),
                    roomType.getRoomTypeName(),
                    roomType.getAmountRoom(),
                    bookedRooms
            );

            dto.setHotelId(roomType.getHotelId());
            dto.setCapacityAdults(roomType.getCapacityAdults());
            dto.setCapacityChildren(roomType.getCapacityChildren());
            dto.setPrice(roomType.getPrice());
            dto.setBreakfastIncluded(roomType.getBreakfastIncluded());
            dto.setFreeCancellation(roomType.getFreeCancellation());
            dto.setCheckinTime(roomType.getCheckinTime());
            dto.setCheckoutTime(roomType.getCheckoutTime());
            dto.setIsActive(roomType.getIsActive());
            dto.setIsDeleted(roomType.getIsDeleted());
            dto.setDateDeleted(roomType.getDateDeleted());
            dto.setRoomTypeAvatar(roomType.getRoomTypeAvatar());
            dto.setRoomTypeDescription(roomType.getRoomTypeDescription());
            dto.setRoomUtilities(new ArrayList<>(roomType.getRoomUtilities()));

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, roomTypesPage.getTotalElements());
    }

    @Override
    public Optional<RoomTypes> findById(String id) {
        return repo.findById(id);
    }

    private int calculateBookedRooms(String roomTypeId, Timestamp checkIn, Timestamp checkOut) {
        Integer bookedRooms = repo.calculateBookedRooms(roomTypeId, checkIn, checkOut);
            return bookedRooms != null ? bookedRooms : 0;
    }

}
