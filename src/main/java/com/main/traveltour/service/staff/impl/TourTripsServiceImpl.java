package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourTrips;
import com.main.traveltour.repository.TourTripsRepository;
import com.main.traveltour.service.staff.TourTripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourTripsServiceImpl implements TourTripsService {

    @Autowired
    private TourTripsRepository repo;

    @Override
    public TourTrips getById(int id) {
        return repo.getById(id);
    }

    @Override
    public Optional<TourTrips> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Page<TourTrips> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<TourTrips> findTourTripsByTourId(String tourDetailId, Pageable pageable) {
        return repo.findTourTripsByTourDetailIdOrderByDayInTripAsc(tourDetailId, pageable);
    }

    @Override
    public List<TourTrips> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Integer> findDayByTourDetailId(String tourDetail) {
        return repo.findDayByTourDetailId(tourDetail);
    }

    @Override
    public List<TourTrips> findTourTripsByTourId(String tourDetailId) {
        return repo.findByTourDetailId(tourDetailId);
    }

    @Override
    public List<TourTrips> findByDayInTripAndTourDetailId(int dayInTrip, String tourDetailId) {
        return repo.findByDayInTripAndTourDetailId(dayInTrip, tourDetailId);
    }

    @Override
    public TourTrips save(TourTrips tourTrips) {
        return repo.save(tourTrips);
    }

    @Override
    public void delete(TourTrips tourTrips) {
        repo.delete(tourTrips);
    }
}
