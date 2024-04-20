package com.main.traveltour.service.admin;


import java.math.BigDecimal;
import java.util.List;

public interface RevenueServiceAD {
    List<BigDecimal> revenueOf12MonthsOfTheYearFromTourBooking(Integer year);

    List<Integer> getAllYearColumn();

    List<Integer> getAllYearPie();
}
