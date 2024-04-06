package com.main.traveltour.service.staff.impl;

import com.main.traveltour.repository.BookingToursRepository;
import com.main.traveltour.repository.TourDetailsRepository;
import com.main.traveltour.service.staff.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RevenueServiceImpl implements RevenueService {

    @Autowired
    private BookingToursRepository bookingToursRepository;
    @Autowired
    private TourDetailsRepository tourDetailsRepository;

    @Override
    public List<Map<String, Object>> getRevenueByYear() {
        List<Map<String, Object>> bookingTourRevenue = bookingToursRepository.getBookingTourRevenueByYear();
        List<Map<String, Object>> cancelOrderRevenue = bookingToursRepository.getCancelOrderRevenueByYear();
        Map<Integer, BigDecimal> combinedRevenue = new HashMap<>(); // Change to BigDecimal

        // Combine booking and cancel order revenues by year
        for (Map<String, Object> revenue : bookingTourRevenue) {
            Integer year = (Integer) revenue.get("year");
            BigDecimal total = Optional.ofNullable((BigDecimal) revenue.get("totalRevenue")).orElse(BigDecimal.ZERO); // Handle BigDecimal
            combinedRevenue.merge(year, total, BigDecimal::add); // Use BigDecimal::add
        }
        for (Map<String, Object> revenue : cancelOrderRevenue) {
            Integer year = (Integer) revenue.get("year");
            BigDecimal total = Optional.ofNullable((BigDecimal) revenue.get("totalRevenue")).orElse(BigDecimal.ZERO); // Handle BigDecimal
            combinedRevenue.merge(year, total, BigDecimal::add); // Use BigDecimal::add
        }

        int currentYear = Year.now().getValue();
        for (int i = 0; i < 4; i++) {
            int targetYear = currentYear - i;
            combinedRevenue.computeIfAbsent(targetYear, k -> BigDecimal.ZERO); // Handle BigDecimal
        }

        List<Map<String, Object>> combinedRevenueList = combinedRevenue.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> revenueMap = new HashMap<>();
                    revenueMap.put("year", entry.getKey());
                    revenueMap.put("totalRevenue", entry.getValue());
                    return revenueMap;
                })
                .sorted((map1, map2) -> Integer.compare((Integer) map1.get("year"), (Integer) map2.get("year")))
                .limit(4)
                .collect(Collectors.toList());

        return combinedRevenueList;
    }

    @Override
    public List<Integer> countCompletedToursByYearAndTourType(Integer tourTypeId, Integer year) {
        return tourDetailsRepository.countCompletedToursByYearAndTourType(tourTypeId, year);
    }

    @Override
    public List<Integer> getAllYear() {
        return tourDetailsRepository.getAllYear();
    }

}
