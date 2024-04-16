package com.main.traveltour.service.agent;

import com.main.traveltour.entity.PlaceUtilities;

import java.util.List;
import java.util.Optional;

public interface PlaceUtilitiesService {

    List<PlaceUtilities> findAll();

    PlaceUtilities findByPlaceId(int id);

    PlaceUtilities save(PlaceUtilities placeUtilities);

    PlaceUtilities findById(int id);

    Optional<PlaceUtilities> findByPlaceUtilsId(int id);
}
