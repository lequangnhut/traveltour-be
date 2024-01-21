package com.main.traveltour.dto.agent;

import lombok.Data;

import java.util.List;

@Data
public class Hotel_RoomDto {

    HotelsDto hotelsDto;

    RoomTypesDto roomTypesDto;

    List<String> selectedRoomUtilitiesIds;

    List<String> selectedPlaceUtilitiesIds;
}
