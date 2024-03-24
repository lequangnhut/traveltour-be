package com.main.traveltour.service.customer;

import com.main.traveltour.entity.OrderTransportationDetails;

import java.util.List;

public interface OrderVehicleDetailsService {

    List<OrderTransportationDetails> findByOrderId(String id);

}
