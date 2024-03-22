package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderVisitDetails;
import com.main.traveltour.repository.OrderVisitDetailsRepository;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderVisitDetailServiceImpl implements OrderVisitDetailService {

    @Autowired
    private OrderVisitDetailsRepository orderVisitDetailsRepository;

    @Override
    public OrderVisitDetails save(OrderVisitDetails orderVisitDetails) {
        return orderVisitDetailsRepository.save(orderVisitDetails);
    }

    @Override
    public List<OrderVisitDetails> findByOrderVisitId(String id) {
        return orderVisitDetailsRepository.findByOrderVisitId(id);
    }
}
