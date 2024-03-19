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

    Page<TourTrips> findTourTripsByTourDetailIdOrderByDayInTripAsc(String tourDetailId, Pageable pageable);

    @Query("SELECT tt.dayInTrip " +
            "FROM TourTrips tt " +
            "WHERE tt.tourDetailId = :tourDetailId " +
            "GROUP BY tt.dayInTrip")
    List<Integer> findDayByTourDetailId(@Param("tourDetailId") String tourDetailId);

    @Query("SELECT tt " +
            "FROM TourTrips tt " +
            "WHERE tt.tourDetailId = :tourDetailId " +
            "AND tt.dayInTrip = 1")
    List<TourTrips> findByTourDetailId(@Param("tourDetailId") String tourDetailId);

    List<TourTrips> findByDayInTripAndTourDetailId(int dayInTrip, String tourDetailId);
}