package com.main.traveltour.service.staff;

import java.util.List;
import java.util.Map;

public interface RevenueService {
    List<Map<String, Object>> getRevenueByYear();

    List<Integer> countCompletedToursByYearAndTourType(Integer tourTypeId, Integer year);

    List<Integer> getAllYear();
}
