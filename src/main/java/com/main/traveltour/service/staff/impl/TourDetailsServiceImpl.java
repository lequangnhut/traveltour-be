package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.repository.TourDetailsRepository;
import com.main.traveltour.service.staff.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TourDetailsServiceImpl implements TourDetailsService {

    @Autowired
    private TourDetailsRepository tourDetailsRepository;

    @Override
    public String getMaxCodeTourDetailId() {
        return tourDetailsRepository.getMaxCodeTourDetailId();
    }

    @Override
    public List<TourDetails> findAll() {
        return tourDetailsRepository.findAll();
    }

    @Override
    public Page<TourDetails> findAll(Pageable pageable) {
        return tourDetailsRepository.findAllTourDetail(pageable);
    }

    @Override
    public Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable) {
        return tourDetailsRepository.findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public Optional<TourDetails> findById(String id) {
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
        LocalDate localDate = LocalDate.now();
        Date currentDate = java.sql.Date.valueOf(localDate);

        List<TourDetails> tourDetails1 = tourDetailsRepository.findAllByDepartureDate(currentDate);
        List<TourDetails> tourDetails2 = tourDetailsRepository.findAllByArrivalDate(currentDate);

        for (TourDetails tourDetails : tourDetails1) {
            tourDetails.setTourDetailStatus(2);
            tourDetailsRepository.save(tourDetails);
        }
        for (TourDetails tourDetails : tourDetails2) {
            tourDetails.setTourDetailStatus(3);
            tourDetailsRepository.save(tourDetails);
        }
    }
}
