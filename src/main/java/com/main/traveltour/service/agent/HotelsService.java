package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
}
