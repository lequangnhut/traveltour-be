package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.*;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.PlaceUtilitiesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.FileUploadResize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelsServiceImpl implements HotelsService {

    @Autowired
    private HotelsRepository hotelsRepository;

    @Autowired
    private PlaceUtilitiesService placeUtilitiesService;

    @Autowired
    private FileUploadResize fileUploadResize;

    @Override
    public List<Hotels> findAllListHotel() {
        return hotelsRepository.findAll();
    }

    @Override
    public List<Hotels> findAllByAgencyId(int agencyId) {
        return hotelsRepository.findAllByAgenciesId(agencyId);
    }

    @Override
    public Hotels findByAgencyId(int agencyId) {
        return hotelsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public String findMaxCode() {
        return hotelsRepository.findMaxCode();
    }

    @Override
    public Hotels save(Hotels hotels) {
        return hotelsRepository.save(hotels);
    }

    @Override
    public Optional<Hotels> findById(String id) {
        return hotelsRepository.findById(id);
    }

    @Override
    public void delete(Hotels hotels) {
        hotelsRepository.save(hotels);
    }

    @Override
    public List<Hotels> getAllHotels() {
        return hotelsRepository.findAll();
    }

    @Override
    public void registerInfoHotel(Hotels hotels, String hotelId, List<Integer> placeUtilities, MultipartFile avatarHotel) throws IOException {

        String avataHotelUpload = fileUploadResize.uploadFileResize(avatarHotel);

        hotels.setId(hotelId);
        hotels.setIsDeleted(false);
        hotels.setDateCreated(Timestamp.valueOf(java.time.LocalDateTime.now()));
        hotels.setIsActive(true);
        hotels.setIsAccepted(true);
        hotels.setHotelAvatar(avataHotelUpload);
        if(hotels.getHotelDescription() == null) {
            hotels.setHotelDescription("Không có");
        }

        List<PlaceUtilities> placeUtilitiesList = placeUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());

        hotels.setPlaceUtilities(placeUtilitiesList);
        hotelsRepository.save(hotels);
    }

}
