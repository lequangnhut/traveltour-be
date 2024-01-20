package com.main.traveltour.service.agent;

import com.main.traveltour.entity.PlaceUtilities;

import java.util.List;

public interface PlaceUtilitiesService {

    List<PlaceUtilities> findAll();

    PlaceUtilities save(PlaceUtilities placeUtilities);
}
