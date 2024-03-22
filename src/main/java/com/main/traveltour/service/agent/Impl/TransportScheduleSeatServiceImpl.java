package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationScheduleSeats;
import com.main.traveltour.repository.TransportationScheduleSeatsRepository;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportScheduleSeatServiceImpl implements TransportScheduleSeatService {

    @Autowired
    private TransportationScheduleSeatsRepository repo;

    @Override
    public List<TransportationScheduleSeats> findAllByScheduleId(String scheduleId) {
        return repo.findAllByTransportationScheduleId(scheduleId);
    }

    @Override
    public List<TransportationScheduleSeats> findAllBySeatNumberScheduleId(int seatNumber, String scheduleId) {
        return repo.findAllBySeatNumberAndTransportationScheduleId(seatNumber, scheduleId);
    }

    @Override
    public List<TransportationScheduleSeats> findAllByScheduleIdAndOrderId(String scheduleId, String orderTransportId) {
        return repo.findAllByScheduleIdAndOrderId(scheduleId, orderTransportId);
    }

    @Override
    public TransportationScheduleSeats save(TransportationScheduleSeats seats) {
        return repo.save(seats);
    }

    @Override
    public void deleteAll(String scheduleId) {
        List<TransportationScheduleSeats> scheduleSeats = repo.findAllByTransportationScheduleId(scheduleId);
        repo.deleteAll(scheduleSeats);
    }
}
