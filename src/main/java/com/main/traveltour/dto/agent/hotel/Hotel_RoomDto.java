package com.main.traveltour.dto.agent.hotel;

import lombok.Data;

import java.util.List;

@Data
public class Hotel_RoomDto {

    HotelsDto hotelsDto;

    RoomTypesDto roomTypesDto;

    List<Integer> selectedRoomUtilitiesIds;

    List<Integer> selectedPlaceUtilitiesIds;
}
