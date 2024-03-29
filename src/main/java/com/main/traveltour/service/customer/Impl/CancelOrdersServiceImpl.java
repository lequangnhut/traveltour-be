package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.CancelOrders;
import com.main.traveltour.repository.CancelOrdersRepository;
import com.main.traveltour.service.customer.CancelOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelOrdersServiceImpl implements CancelOrdersService {

    @Autowired
    CancelOrdersRepository cancelOrdersRepository;

    @Override
    public CancelOrders save(CancelOrders cancelOrders) {
        return cancelOrdersRepository.save(cancelOrders);
    }
}
