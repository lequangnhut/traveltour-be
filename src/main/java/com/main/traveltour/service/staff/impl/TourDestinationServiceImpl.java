package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourDestinations;
import com.main.traveltour.repository.TourDestinationsRepository;
import com.main.traveltour.service.staff.TourDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourDestinationServiceImpl implements TourDestinationService {

    @Autowired
    private TourDestinationsRepository repo;


    @Override
    public List<TourDestinations> findAllByTourDetailId(String tourDetailId) {
        return repo.findAllByTourDetailId(tourDetailId);
    }

    @Override
    public TourDestinations findByTourDetailId(String tourDetailId) {
        return repo.findByTourDetailId(tourDetailId);
    }

    @Override
    public TourDestinations save(TourDestinations tourDestinations) {
        return repo.save(tourDestinations);
    }

    @Override
    public void deleteAll(String tourDetailId) {
        List<TourDestinations> destinations = findAllByTourDetailId(tourDetailId);
        repo.deleteAll(destinations);
    }
}
