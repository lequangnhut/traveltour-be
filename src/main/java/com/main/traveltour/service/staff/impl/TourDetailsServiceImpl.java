package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.repository.TourDetailsRepository;
import com.main.traveltour.service.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TourDetailsServiceImpl implements TourDetailsService {

    @Autowired
    private TourDetailsRepository tourDetailsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getMaxCodeTourDetailId() {
        return tourDetailsRepository.getMaxCodeTourDetailId();
    }

    @Override
    public List<TourDetails> findAll() {
        return tourDetailsRepository.getAllTourDetail();
    }

    @Override
    public List<TourDetails> getAllJoinBooking() {
        return tourDetailsRepository.getAllJoinBooking();
    }

    @Override
    public List<Object[]> findTourTrend() {
        return tourDetailsRepository.findTourDetailTrend();
    }

    @Override
    public Page<TourDetails> findAll(Pageable pageable) {
        return tourDetailsRepository.findAllTourDetail(pageable);
    }

    @Override
    public Page<TourDetails> findTourDetailWithFilter(String searchTerm, Date departureDate, BigDecimal price, List<Integer> tourTypesByTourTypeId, Pageable pageable) {
        return tourDetailsRepository.findTourDetailWithFilter(searchTerm, departureDate, price, tourTypesByTourTypeId, pageable);
    }


    @Override
    public Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable) {
        return tourDetailsRepository.findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public TourDetails findById(String id) {
        return tourDetailsRepository.findById(id);
    }

    @Override
    public TourDetails getById(String id) {
        return tourDetailsRepository.getById(id);
    }

    @Override
    public TourDetails save(TourDetails tourDetails) {
        return tourDetailsRepository.save(tourDetails);
    }

    @Override
    public void delete(TourDetails tourDetails) {
        tourDetailsRepository.delete(tourDetails);
    }

    @Override
    public void updateStatusAndActive() {
        List<TourDetails> tourDetails1 = tourDetailsRepository.findTourInProgress();
        List<TourDetails> tourDetails2 = tourDetailsRepository.findTourCompleted();

        for (TourDetails tourDetails : tourDetails1) {
            tourDetails.setTourDetailStatus(2);
            saveTourDetail(tourDetails);
        }
        for (TourDetails tourDetails : tourDetails2) {
            tourDetails.setTourDetailStatus(3);
            saveTourDetail(tourDetails);
        }
    }

    public void saveTourDetail(TourDetails tourDetails) {
        String sql = "UPDATE tour_details SET tour_detail_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, tourDetails.getTourDetailStatus(), tourDetails.getId());
    }
}
