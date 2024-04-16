package com.main.traveltour.service.agent;

import com.main.traveltour.dto.agent.hotel.RegisterHotelDto;
import com.main.traveltour.dto.agent.hotel.RegisterRoomTypeDto;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HotelsService {

    List<Hotels> findAllListHotel();

    List<Hotels> findAllByAgencyId(int agencyId);

    Hotels findByAgencyId(int agencyId);

    String findMaxCode();

    Hotels save(Hotels hotels);

    Optional<Hotels> findById(String id);

    void delete(Hotels hotels);

    List<Hotels> getAllHotels();

    void registerInfoHotel(Hotels hotels, String hotelId, List<Integer> placeUtilities, MultipartFile avatarHotel) throws IOException;
    void registerInfoHotel(
            RegisterHotelDto hotels, MultipartFile avatarHotel, List<Integer> placeUtilities,
            RegisterRoomTypeDto roomTypes, MultipartFile avatarRoomTypes, List<MultipartFile> listImagesRoomTypes, List<Integer> roomTypeUtilities,
            Integer bedTypeId, LocalTime checkinTime, LocalTime checkoutTime
    ) throws IOException;
    List<Hotels> findHotelsByAgenciesIdAndIsDeleted(Integer agentsId);
}
