package com.main.traveltour.service.staff;

import com.main.traveltour.entity.OrderVisitDetails;

import java.util.List;

public interface OrderVisitLocationDetailService {
    void save(OrderVisitDetails orderVisitDetails);

    List<OrderVisitDetails> findByOrderVisitId(String orderId);
}
