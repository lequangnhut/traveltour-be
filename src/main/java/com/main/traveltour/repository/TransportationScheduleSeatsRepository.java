package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationScheduleSeats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransportationScheduleSeatsRepository extends JpaRepository<TransportationScheduleSeats, Integer> {

    List<TransportationScheduleSeats> findAllByTransportationScheduleId(String scheduleId);

    @Query("SELECT seat FROM TransportationScheduleSeats seat WHERE seat.delayBooking >= :startTimestamp AND seat.delayBooking <= :endTimestamp")
    List<TransportationScheduleSeats> findAllByDelayBookingBetween(Timestamp startTimestamp, Timestamp endTimestamp);

    List<TransportationScheduleSeats> findAllBySeatNumberAndTransportationScheduleId(int seatNumber, String scheduleId);

    @Query("SELECT tps FROM TransportationScheduleSeats tps " +
            "JOIN tps.orderTransportationDetailById ord " +
            "WHERE tps.transportationScheduleId = :scheduleId AND ord.OrderTransportationId = :orderTransportId")
    List<TransportationScheduleSeats> findAllByScheduleIdAndOrderId(@Param("scheduleId") String scheduleId, @Param("orderTransportId") String orderTransportId);

    @Query("SELECT tss FROM TransportationScheduleSeats tss JOIN tss.orderTransportationDetailById otd " +
            "JOIN otd.orderTransportationById ot WHERE ot.id = :id ")
    List<TransportationScheduleSeats> findSeatByOrderTd(String id);

}