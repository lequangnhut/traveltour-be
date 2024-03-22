package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.OrderTransportationDetails;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.admin.HotelsServiceAD;
import com.main.traveltour.service.admin.OrderTransportationDetailsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTransportationDetailsServiceImplAD implements OrderTransportationDetailsServiceAD {

    @Autowired
    private HotelsRepository hotelsRepository;


    @Override
    public List<OrderTransportationDetails> findByOrderId(String id) {
        return null;
    }
}
