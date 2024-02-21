package com.main.traveltour.repository;

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

    @Query("SELECT COALESCE(MAX(tt.dayInTrip),0) FROM TourTrips tt WHERE tt.tourDetailId = :tourDetailId")
    int getDayInTripIsMax(@Param("tourDetailId") String id);

    List<TourTrips> findTourTripsByTourDetailId(String tourDetailId);

    Page<TourTrips> findTourTripsByTourDetailId(String tourDetailId, Pageable pageable);
}