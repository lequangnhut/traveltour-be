package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDestinations;

import java.util.List;

public interface TourDestinationService {

    List<TourDestinations> findAllByTourDetailId(String tourDetailId);

    TourDestinations findByTourDetailId(String tourDetailId);

    TourDestinations save(TourDestinations tourDestinations);

    void deleteAll(String tourDetailId);
}
