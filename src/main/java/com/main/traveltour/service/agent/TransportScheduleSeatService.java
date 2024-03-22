package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationScheduleSeats;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportScheduleSeatService {

    List<TransportationScheduleSeats> findAllByScheduleId(String scheduleId);

    List<TransportationScheduleSeats> findAllBySeatNumberScheduleId(int seatNumber, String scheduleId);

    List<TransportationScheduleSeats> findAllByScheduleIdAndOrderId(String scheduleId, String orderTransportId);

    TransportationScheduleSeats save(TransportationScheduleSeats seats);

    void deleteAll(String scheduleId);
}
