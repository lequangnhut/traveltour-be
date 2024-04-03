package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.PaymentMethod;
import com.main.traveltour.repository.PaymentMethodRepository;
import com.main.traveltour.service.agent.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Override
    public List<PaymentMethod> getAllPaymentMethod() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod findById(String id) {
        return paymentMethodRepository.findById(id).get();
    }
}
