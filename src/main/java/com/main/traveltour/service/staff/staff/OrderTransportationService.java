package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.OrderVisits;

public interface OrderTransportationService {
    String maxCode();

    OrderTransportations save(OrderTransportations orderTransportations);
}
