package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.*;
import com.main.traveltour.repository.*;
import com.main.traveltour.service.staff.BookingTourHotelService;
import com.main.traveltour.service.staff.BookingTourVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingTourVisitServiceImpl implements BookingTourVisitService {

    @Autowired
    private VisitLocationsRepository visitLocationsRepository;

    @Autowired
    private OrderVisitsRepository orderVisitsRepository;

    @Autowired
    private OrderVisitDetailsRepository orderVisitDetailsRepository;


    @Override
    public Page<VisitLocations> findVisitByTourDetailId(String tourDetailId, Integer orderVisitStatus, String searchTerm, Pageable pageable) {
        return visitLocationsRepository.findVisitByTourDetailId(tourDetailId, orderVisitStatus, searchTerm, pageable);
    }

    @Override
    public List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitId(String tourDetailId, String visitId, Integer orderVisitStatus) {
        return orderVisitDetailsRepository.findOrderVisitDetailByTourDetailIdAndVisitId(tourDetailId, visitId, orderVisitStatus);
    }

    @Override
    public List<OrderVisits> findOrderVisitByTourDetailIdAndVisitId(String tourDetailId, String visitId, Integer orderVisitStatus) {
        return orderVisitsRepository.findOrderVisitByTourDetailIdAndVisitId(tourDetailId, visitId, orderVisitStatus);
    }

    @Override
    public void update(OrderVisits orderVisits) {
        orderVisitsRepository.save(orderVisits);
    }

    @Override
    public Page<VisitLocations> findVisitByTourDetailIdGuide(String tourDetailId, String searchTerm, Pageable pageable) {
        return visitLocationsRepository.findVisitByTourDetailIdForGuide(tourDetailId, searchTerm, pageable);
    }

    @Override
    public List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitIdGuide(String tourDetailId, String visitId) {
        return orderVisitDetailsRepository.findOrderVisitDetailByTourDetailIdAndVisitIdForGuide(tourDetailId, visitId);
    }
}
