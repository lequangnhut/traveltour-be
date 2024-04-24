package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.repository.OrderVisitsRepository;
import com.main.traveltour.service.agent.OrderVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderVisitServiceImpl implements OrderVisitService {

    @Autowired
    private OrderVisitsRepository orderVisitsRepository;

    @Override
    public String findMaxCode() {
        return orderVisitsRepository.findMaxCode();
    }

    @Override
    public OrderVisits findById(String id) {
        return orderVisitsRepository.findById(id);
    }

    @Override
    public OrderVisits save(OrderVisits orderVisits) {
        return orderVisitsRepository.save(orderVisits);
    }

    @Override
    public Page<OrderVisits> findAllOrderVisits(String brandId, Pageable pageable) {
        return orderVisitsRepository.findAllOrderVisits(brandId, pageable);
    }

    @Override
    public Page<OrderVisits> findAllOrderVisitsWithSearch(String brandId, String searchTerm, Pageable pageable) {
        return orderVisitsRepository.findAllOrderVisitsWithSearch(brandId, searchTerm, pageable);
    }

    @Override
    public Optional<OrderVisits> findByIdOptional(String id) {
        return Optional.ofNullable(orderVisitsRepository.findById(id));
    }

    @Override
    public List<Double> getStatisticalBookingVisitLocation(Integer year, String visitId) {
        List<Double> percentages = new ArrayList<>();
        long totalVisitLocation = 0;

        for (int orderStatus : Arrays.asList(0, 1, 2)) {
            Long revenue = orderVisitsRepository.getStatisticalBookingVisitLocation(year, orderStatus, visitId);
            totalVisitLocation += revenue;
        }

        if (totalVisitLocation != 0) {
            for (int orderStatus : Arrays.asList(0, 1, 2)) {
                Long revenue = orderVisitsRepository.getStatisticalBookingVisitLocation(year, orderStatus, visitId);
                double percentage = ((double) revenue / totalVisitLocation) * 100;
                double roundedPercentage = Math.round(percentage * 10) / 10.0;
                percentages.add(roundedPercentage);
            }
        } else {
            percentages.addAll(Arrays.asList(0.0, 0.0, 0.0));
        }
        return percentages;
    }

}
