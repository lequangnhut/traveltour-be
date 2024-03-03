package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderVisitDetails;
import com.main.traveltour.repository.OrderHotelDetailsRepository;
import com.main.traveltour.repository.OrderVisitDetailsRepository;
import com.main.traveltour.service.staff.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.staff.OrderVisitLocationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderVisitLocationDetailServiceImpl implements OrderVisitLocationDetailService {
    @Autowired
    OrderVisitDetailsRepository repo;

    @Override
    public void save(OrderVisitDetails orderVisitDetails) {
        repo.save(orderVisitDetails);
    }
}
