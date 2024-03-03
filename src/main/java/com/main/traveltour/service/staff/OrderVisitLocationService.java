package com.main.traveltour.service.staff;

import com.main.traveltour.entity.OrderVisits;

public interface OrderVisitLocationService {
    String maxCode();

    OrderVisits save(OrderVisits orderVisits);
}
