package com.main.traveltour.service.agent;

import com.main.traveltour.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethod> getAllPaymentMethod();

    PaymentMethod findById(String id);
}
