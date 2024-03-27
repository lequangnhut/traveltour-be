package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationScheduleSeats;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationScheduleSeatsRepository;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Service
public class TransportScheduleSeatServiceImpl implements TransportScheduleSeatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    public void checkTimeBookingTransport() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.minusMinutes(10);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(now);

        List<TransportationScheduleSeats> scheduleSeats = repo.findAllByDelayBookingBetween(startTimestamp, endTimestamp);

        for (TransportationScheduleSeats seats : scheduleSeats) {
            seats.setIsBooked(Boolean.FALSE);
            seats.setDelayBooking(null);
            saveTransportScheduleSeat(seats);
        }
    }

    public void saveTransportScheduleSeat(TransportationScheduleSeats scheduleSeats) {
        String sql = "UPDATE transportation_schedule_seats SET is_booked = ?, delay_booking = ? WHERE id = ?";
        int isBookedValue = scheduleSeats.getIsBooked() ? 1 : 0;
        jdbcTemplate.update(sql, isBookedValue, scheduleSeats.getDelayBooking(), scheduleSeats.getId());
    }
}
