package com.main.traveltour.service.customer.Impl;


import com.main.traveltour.entity.OrderTransportationDetails;
import com.main.traveltour.repository.OrderTransportationDetailsRepository;
import com.main.traveltour.service.customer.OrderVehicleDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OrderVehicleDetailServiceImpl implements OrderVehicleDetailsService {

    @Autowired
    OrderTransportationDetailsRepository orderTransportationDetailsRepository;

    @Override
    public List<OrderTransportationDetails> findByOrderId(String id) {
        return orderTransportationDetailsRepository.findALlByOrderId(id);
    }
}
