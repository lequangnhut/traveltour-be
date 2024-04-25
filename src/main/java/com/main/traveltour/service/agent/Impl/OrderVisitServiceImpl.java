package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.repository.OrderVisitsRepository;
import com.main.traveltour.service.agent.OrderVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    public List<Long[]> getNumberOfAdultTickets(Integer year, String visitId) {
        List<Long[]> listNumberOfTickets = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            Object[] numberOfTicketsThisYear = orderVisitsRepository.getNumberOfAdultTickets(year, i, visitId);
            Object[] numberOfTicketsLastYear = orderVisitsRepository.getNumberOfAdultTickets(year - 1, i, visitId);
            Long[] ticketCounts = new Long[4];
            ticketCounts[0] = (Long) ((Object[]) numberOfTicketsThisYear[0])[0];
            ticketCounts[1] = (Long) ((Object[]) numberOfTicketsThisYear[0])[1]; // Số vé trẻ em năm nay
            ticketCounts[2] = (Long) ((Object[]) numberOfTicketsLastYear[0])[0]; // Số vé người lớn năm trước
            ticketCounts[3] = (Long) ((Object[]) numberOfTicketsLastYear[0])[1]; // Số vé trẻ em năm trước

            listNumberOfTickets.add(ticketCounts);
        }

        return listNumberOfTickets;
    }

    @Override
    public List<BigDecimal[]> getRevenueOfTouristAttractions(Integer year, String visitId) {
        List<BigDecimal[]> listNumberOfTickets = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            BigDecimal revenueThisYear = orderVisitsRepository.getRevenueOfTouristAttractions(year, i, visitId);
            BigDecimal revenueLastYear = orderVisitsRepository.getRevenueOfTouristAttractions(year - 1, i, visitId);
            BigDecimal[] ticketCounts = new BigDecimal[2];
            ticketCounts[0] = revenueThisYear;
            ticketCounts[1] = revenueLastYear;

            listNumberOfTickets.add(ticketCounts);
        }

        return listNumberOfTickets;
    }

    @Override
    public List<Integer> getAllOrderVisitYear() {
        return orderVisitsRepository.getAllOrderVisitYear();
    }


}
