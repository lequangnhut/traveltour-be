package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.PlaceUtilities;
import com.main.traveltour.repository.PlaceUtilitiesRepository;
import com.main.traveltour.service.agent.PlaceUtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceUtilitiesServiceImpl implements PlaceUtilitiesService {

    @Autowired
    private PlaceUtilitiesRepository placeUtilitiesRepository;

    @Override
    public List<PlaceUtilities> findAll() {
        return placeUtilitiesRepository.findAll();
    }

    @Override
    public PlaceUtilities findByPlaceId(int id) {
        return placeUtilitiesRepository.findById(id);
    }

    @Override
    public PlaceUtilities save(PlaceUtilities placeUtilities) {
        return placeUtilitiesRepository.save(placeUtilities);
    }
}
