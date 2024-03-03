package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.repository.TourDetailImagesRepository;
import com.main.traveltour.service.staff.TourDetailsImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourDetailsImageServiceImpl implements TourDetailsImageService {

    @Autowired
    private TourDetailImagesRepository tourDetailImagesRepository;

    @Override
    public TourDetailImages save(TourDetailImages tourDetailImages) {
        return tourDetailImagesRepository.save(tourDetailImages);
    }

    @Override
    public void delete(String tourDetailId) {
        List<TourDetailImages> imagesToDelete = tourDetailImagesRepository.findAllByTourDetailId(tourDetailId);
        tourDetailImagesRepository.deleteAll(imagesToDelete);
    }
}
