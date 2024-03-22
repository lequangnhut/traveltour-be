package com.main.traveltour.service.agent;

import com.main.traveltour.entity.OrderVisitDetails;

import java.util.List;

public interface OrderVisitDetailService {

    OrderVisitDetails save(OrderVisitDetails orderVisitDetails);

    List<OrderVisitDetails>  findByOrderVisitId(String id);
}
