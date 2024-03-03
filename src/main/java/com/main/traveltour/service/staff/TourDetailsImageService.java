package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDetailImages;

public interface TourDetailsImageService {

    TourDetailImages save(TourDetailImages tourDetailImages);

    void delete(String tourDetailId);
}
