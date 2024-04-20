package com.main.traveltour.service.admin.impl;

import com.main.traveltour.repository.BookingToursRepository;
import com.main.traveltour.service.admin.RevenueServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RevenueServiceImplAD implements RevenueServiceAD {
    @Autowired
    private BookingToursRepository repo;

    @Override
    public List<BigDecimal> revenueOf12MonthsOfTheYearFromTourBooking(Integer year) {
        return repo.revenueOf12MonthsOfTheYearFromTourBooking(year);
    }

    @Override
    public List<Integer> getAllYearColumn() {
        return repo.getAllYearColumn();
    }

    @Override
    public List<Integer> getAllYearPie() {
        return repo.getAllYearPie();
    }
}
