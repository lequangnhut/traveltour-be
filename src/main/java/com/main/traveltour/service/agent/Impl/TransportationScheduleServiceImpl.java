package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.agent.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransportationScheduleServiceImpl implements TransportationScheduleService {

    @Autowired
    private TransportationSchedulesRepository repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String findMaxCode() {
        return repo.fixMaxCode();
    }

    @Override
    public TransportationSchedules findBySchedulesId(String transportSchedulesId) {
        return repo.findById(transportSchedulesId);
    }

    @Override
    public TransportationSchedules findCarIsSubmittedAgent(String transportSchedulesId) {
        return repo.findCarIsSubmittedAgent(transportSchedulesId);
    }

    @Override
    public List<TransportationSchedules> findByTransportId(String transportId) {
        return repo.findByTransportationId(transportId);
    }

    @Override
    public List<TransportationSchedules> findAllScheduleByBrandIdRequestCar(String transportBrandId) {
        return repo.findAllScheduleByBrandIdRequestCarAgent(transportBrandId);
    }

    @Override
    public List<TransportationSchedules> findAllScheduleByBrandId(String transportBrandId) {
        return repo.findAllScheduleByBrandId(transportBrandId);
    }

    @Override
    public List<TransportationSchedules> getAllFromLocationAndToLocation() {
        return repo.getAllFromLocationAndToLocation();
    }

    @Override
    public TransportationSchedules save(TransportationSchedules schedules) {
        return repo.save(schedules);
    }

    @Override
    public Page<TransportationSchedules> findAllScheduleAgent(String transportBrandId, Boolean tripType, Pageable pageable) {
        return repo.findAllSchedulesAgent(transportBrandId, tripType, pageable);
    }

    @Override
    public Page<TransportationSchedules> findAllScheduleAgentWitchSearch(String transportBrandId, Boolean tripType, String searchTerm, Pageable pageable) {
        return repo.findAllSchedulesAgentWithSearch(transportBrandId, tripType, searchTerm, pageable);
    }

    @Override
    public Page<TransportationSchedules> findAllTransportScheduleCusFilters(String brandId, BigDecimal price, String fromLocation, String toLocation, Date checkInDateFiller, List<Integer> mediaTypeList, List<String> listOfVehicleManufacturers, Pageable pageable) {
        return repo.findAllTransportScheduleCusFilters(brandId, price, fromLocation, toLocation, checkInDateFiller, mediaTypeList, listOfVehicleManufacturers, pageable);
    }

    @Override
    public void updateStatusAndActive() {
        List<TransportationSchedules> transportationSchedules1 = repo.findTripInProgress();
        List<TransportationSchedules> transportationSchedule2 = repo.findTripCompleted();

        for (TransportationSchedules schedules : transportationSchedules1) {
            schedules.setIsStatus(1);
            saveTransportSchedules(schedules);
        }
        for (TransportationSchedules schedules : transportationSchedule2) {
            schedules.setIsStatus(2);
            saveTransportSchedules(schedules);
        }
    }

    public void saveTransportSchedules(TransportationSchedules schedules) {
        String sql = "UPDATE transportation_schedules SET is_status = ? WHERE id = ? AND trip_type = false";
        jdbcTemplate.update(sql, schedules.getIsStatus(), schedules.getId());
    }
}
