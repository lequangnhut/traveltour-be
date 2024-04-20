package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.*;
import com.main.traveltour.repository.*;
import com.main.traveltour.service.staff.BookingTourTransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingTourTransportationServiceImpl implements BookingTourTransportationService {

    @Autowired
    private TransportationSchedulesRepository transportationSchedulesRepository;


    @Autowired
    private OrderTransportationsRepository orderTransportationsRepository;


    @Override
    public Page<TransportationSchedules> findTransportationSchedulesByTourDetailId(String tourDetailId, Integer orderStatus, String searchTerm, Pageable pageable) {
        return transportationSchedulesRepository.findTransportationSchedulesByTourDetailId(tourDetailId, orderStatus, searchTerm, pageable);
    }

    @Override
    public void update(OrderTransportations orderTransportations) {
        orderTransportationsRepository.save(orderTransportations);
    }

    @Override
    public Page<TransportationSchedules> findTransportationSchedulesByTourDetailIdForGuide(String tourDetailId, String searchTerm, Pageable pageable) {
        return transportationSchedulesRepository.findTransportationSchedulesByTourDetailIdForGuide(tourDetailId, searchTerm, pageable);
    }
}
