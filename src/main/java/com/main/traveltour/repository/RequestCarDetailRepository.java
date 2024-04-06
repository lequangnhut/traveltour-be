package com.main.traveltour.repository;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCarDetailRepository extends JpaRepository<RequestCarDetail, Integer> {

    Page<RequestCarDetail> findAllByRequestCarId(Integer requestCarId, Pageable pageable);

    @Query("SELECT rqcd FROM RequestCarDetail rqcd " +
            "JOIN rqcd.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "WHERE rqcd.id = :requestCarDetail AND t.id = :transportationId")
    RequestCarDetail findByRequestCarIdAndTransportationId(@Param("requestCarDetail") Integer requestCarDetail, @Param("transportationId") String transportationId);
}