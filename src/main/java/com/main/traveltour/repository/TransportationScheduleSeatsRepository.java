package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationScheduleSeats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationScheduleSeatsRepository extends JpaRepository<TransportationScheduleSeats, Integer> {

    List<TransportationScheduleSeats> findAllByTransportationScheduleId(String scheduleId);

    List<TransportationScheduleSeats> findAllBySeatNumberAndTransportationScheduleId(int seatNumber, String scheduleId);

    @Query("SELECT tps FROM TransportationScheduleSeats tps " +
            "JOIN tps.orderTransportationDetailById ord " +
            "WHERE tps.transportationScheduleId = :scheduleId AND ord.OrderTransportationId = :orderTransportId")
    List<TransportationScheduleSeats> findAllByScheduleIdAndOrderId(@Param("scheduleId") String scheduleId, @Param("orderTransportId") String orderTransportId);
}