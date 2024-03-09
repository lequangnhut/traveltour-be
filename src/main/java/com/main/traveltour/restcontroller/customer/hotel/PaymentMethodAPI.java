package com.main.traveltour.restcontroller.customer.hotel;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class PaymentMethodAPI {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("customer/payment-methods/findAllPaymentMethod")
    public ResponseObject findAllPaymentMethod() {
        return new ResponseObject("200", "OK", paymentMethodService.getAllPaymentMethod());
    }
}
