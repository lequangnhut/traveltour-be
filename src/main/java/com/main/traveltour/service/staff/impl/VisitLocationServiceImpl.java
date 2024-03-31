package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.customer.visit.VisitLocationTrendDTO;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.staff.VisitLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VisitLocationServiceImpl implements VisitLocationService {

    @Autowired
    private VisitLocationsRepository repo;

    @Override
    public VisitLocations findByIdAndIsActiveIsTrue(String id) {
        return repo.findByIdAndIsActiveIsTrue(id);
    }

    @Override
    public Page<VisitLocations> getAllByIsActiveIsTrueAndIsAcceptedIsTrue(Pageable pageable) {
        return repo.getAllByIsActiveIsTrueAndIsAcceptedIsTrue(pageable);
    }

    @Override
    public Page<VisitLocations> findBySearchTerm(String searchTerm, Pageable pageable) {
        return repo.findBySearchTerm(searchTerm, pageable);
    }

    @Override
    public Page<VisitLocations> findVisitLocationsByProvince(String location, Pageable pageable) {
        return repo.findVisitLocationsByProvince(location, pageable);
    }

    @Override
    public Page<VisitLocations> findByFilters(String searchTerm, BigDecimal price, List<String> tickerTypeList, List<Integer> locationTypeList, Pageable pageable) {
        return repo.findByFilters(searchTerm, price, tickerTypeList, locationTypeList, pageable);
    }

    @Override
    public List<VisitLocationTrendDTO> findVisitLocationsTrend() {
        return repo.findVisitLocationsTrend();
    }

    @Override
    public List<VisitLocations> getAllVisitLocation() {
        return repo.getAllVisitLocation();
    }

    @Override
    public Long countVisit() {
        return repo.countVisitLocations();
    }
}
