package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.Tours;
import com.main.traveltour.repository.ToursRepository;
import com.main.traveltour.service.staff.ToursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToursServiceImpl implements ToursService {

    @Autowired
    private ToursRepository toursRepository;

    @Override
    public String maxCodeTourId() {
        return toursRepository.maxCodeTourId();
    }

    @Override
    public List<Tours> findAllByIsActiveIsTrue() {
        return toursRepository.findAllByIsActiveIsTrue();
    }

    @Override
    public Page<Tours> findAllByIsActiveIsTrue(Pageable pageable) {
        return toursRepository.findAllByIsActiveIsTrue(pageable);
    }

    @Override
    public Page<Tours> findAllWithSearch(String searchTerm, Pageable pageable) {
        return toursRepository.findByTourNameContainingIgnoreCaseAndIsActiveIsTrue(searchTerm, pageable);
    }

    @Override
    public Optional<Tours> findById(String tourId) {
        return toursRepository.findById(tourId);
    }

    @Override
    public String getToursNameByTourId(String tourId) {
        return toursRepository.getToursNameByTourId(tourId);
    }

    @Override
    public Tours save(Tours tours) {
        return toursRepository.save(tours);
    }

    @Override
    public void saveAll(List<Tours> tours) {
        toursRepository.saveAll(tours);
    }
}
