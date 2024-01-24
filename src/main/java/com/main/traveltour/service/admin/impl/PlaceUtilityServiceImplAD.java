package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.PlaceUtilities;
import com.main.traveltour.entity.RoomUtilities;
import com.main.traveltour.repository.PlaceUtilitiesRepository;
import com.main.traveltour.repository.RoomUtilitiesRepository;
import com.main.traveltour.service.admin.PlaceUtilitiesServiceAD;
import com.main.traveltour.service.admin.RoomUilityServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlaceUtilityServiceImplAD implements PlaceUtilitiesServiceAD {

    @Autowired
    PlaceUtilitiesRepository placeUtilitiesRepository;

    @Override
    public Page<PlaceUtilities> findAll(Pageable pageable) {
        return placeUtilitiesRepository.findAll(pageable);
    }

    @Override
    public Page<PlaceUtilities> findAllWithSearch(String searchTerm, Pageable pageable) {
        return placeUtilitiesRepository.findByPlaceUtilitiesNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public PlaceUtilities findByPlaceUtilityName(String name) {
        return placeUtilitiesRepository.findByPlaceUtilitiesName(name);
    }

    @Override
    public PlaceUtilities findById(int id) {
        return placeUtilitiesRepository.findById(id);
    }

    @Override
    public PlaceUtilities save(PlaceUtilities placeUtilities) {
        return placeUtilitiesRepository.save(placeUtilities);
    }

    @Override
    public PlaceUtilities delete(int id) {
        placeUtilitiesRepository.deleteById(id);
        return null;
    }
}
