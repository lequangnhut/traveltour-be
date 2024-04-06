package com.main.traveltour.repository;

import com.main.traveltour.entity.RequestCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCarRepository extends JpaRepository<RequestCar, Integer> {

    @Query("SELECT rqc FROM RequestCar rqc " +
            "JOIN rqc.requestCarDetailsById rqcd " +
            "JOIN rqcd.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId br " +
            "WHERE rqc.id = :requestCarId AND br.id = :transportBrandId")
    RequestCar findTransportBrandSubmitted(@Param("requestCarId") Integer requestCarId, @Param("transportBrandId") String transportBrandId);
}
