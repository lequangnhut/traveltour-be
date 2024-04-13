package com.main.traveltour.repository;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestCarDetailRepository extends JpaRepository<RequestCarDetail, Integer> {

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "WHERE rqcd.transportationScheduleId = :transportationScheduleId AND rqcd.isAccepted = 1")
    RequestCarDetail findRequestCarDetailSubmitted(@Param("transportationScheduleId") String transportationScheduleId);

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "WHERE rqcd.transportationScheduleId = :transportationScheduleId " +
            "AND rqcd.requestCarId = :requestCarId " +
            "AND rqcd.isAccepted = 1")
    RequestCarDetail findCarSubmitted(@Param("requestCarId") Integer requestCarId,
                                      @Param("transportationScheduleId") String transportationScheduleId);

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "WHERE rqcd.requestCarId = :requestCarId " +
            "AND rqcd.isAccepted = 0")
    List<RequestCarDetail> findAllByRequestCarId(@Param("requestCarId") Integer requestCarId);

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "WHERE rqcd.requestCarId = :requestCarId AND (rqcd.isAccepted = 0 OR rqcd.isAccepted = 1 OR rqcd.isAccepted = 3)")
    Page<RequestCarDetail> findAllByRequestCarId(@Param("requestCarId") Integer requestCarId,
                                                 Pageable pageable);

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "JOIN rqcd.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "JOIN t.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId AND rqcd.isAccepted = :acceptedRequest")
    Page<RequestCarDetail> findAllHistoryRequestCar(@Param("acceptedRequest") Integer acceptedRequest,
                                                    @Param("transportBrandId") String transportBrandId,
                                                    Pageable pageable);
}