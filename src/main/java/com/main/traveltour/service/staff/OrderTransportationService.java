package com.main.traveltour.service.staff;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.OrderVisits;

import java.util.List;

public interface OrderTransportationService {
    String maxCode();

    List<OrderTransportations> findAllByTransportationScheduleId(String transportationScheduleId);

    OrderTransportations save(OrderTransportations orderTransportations);

    void update(OrderTransportations orderTransportations);
}
