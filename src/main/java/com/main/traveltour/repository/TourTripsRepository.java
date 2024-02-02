package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TourTrips;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourTripsRepository extends JpaRepository<TourTrips, Integer> {
    TourTrips getById(int id);

    @Query("SELECT COALESCE(MAX(tt.dayInTrip),0) FROM TourTrips tt WHERE tt.tourId = :tourId")
    int getDayInTripIsMax(@Param("tourId") String id);

    List<TourTrips> findTourTripsByTourId(String tourId);

    Page<TourTrips> findTourTripsByTourId(String tourId, Pageable pageable);
}