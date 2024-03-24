package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TransportationScheduleSeats;
import com.main.traveltour.repository.TransportationScheduleSeatsRepository;
import com.main.traveltour.service.admin.TransportationSeatServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationSeatServiceImplAD implements TransportationSeatServiceAD {

    @Autowired
    private TransportationScheduleSeatsRepository transportationScheduleSeatsRepository;


    @Override
    public List<TransportationScheduleSeats> findSeatByOrderTd(String id) {
        return transportationScheduleSeatsRepository.findSeatByOrderTd(id);
    }

    @Override
    public TransportationScheduleSeats save(TransportationScheduleSeats seats) {
        return transportationScheduleSeatsRepository.save(seats);
    }
}
