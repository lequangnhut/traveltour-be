package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourTrips;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TourTripsService {

    TourTrips getById(int id);

    List<TourTrips> findByDayInTripAndTourDetailId(int dayInTrip, String tourDetailId);

    List<Integer> findDayByTourDetailId(String tourDetail);

    List<TourTrips> findAll();

    Page<TourTrips> findAll(Pageable pageable);

    Optional<TourTrips> findById(int id);

    List<TourTrips> findTourTripsByTourId(String tourId);

    Page<TourTrips> findTourTripsByTourId(String tourId, Pageable pageable);

    TourTrips save(TourTrips tourTrips);

    void delete(TourTrips tourTrips);
}
