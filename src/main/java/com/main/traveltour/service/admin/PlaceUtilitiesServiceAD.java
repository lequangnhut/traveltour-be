package com.main.traveltour.service.admin;

import com.main.traveltour.entity.PlaceUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceUtilitiesServiceAD {

    Page<PlaceUtilities> findAll(Pageable pageable);

    Page<PlaceUtilities> findAllWithSearch(String searchTerm, Pageable pageable);

    PlaceUtilities findByPlaceUtilityName(String name);

    PlaceUtilities findById(int id);

    PlaceUtilities save(PlaceUtilities placeUtilities);

    PlaceUtilities delete(int id);

}
