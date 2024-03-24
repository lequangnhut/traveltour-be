package com.main.traveltour.service.admin;

import com.main.traveltour.entity.TransportationScheduleSeats;

import java.util.List;

public interface TransportationSeatServiceAD {

    List<TransportationScheduleSeats> findSeatByOrderTd (String id);

    TransportationScheduleSeats save(TransportationScheduleSeats seats);
}
