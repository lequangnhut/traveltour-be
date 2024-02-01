package com.main.traveltour.service.agent;

import com.main.traveltour.entity.OrderVisits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderVisitService {

    String findMaxCode();

    OrderVisits findById(String id);

    OrderVisits save(OrderVisits orderVisits);

    Page<OrderVisits> findAllOrderVisits(String brandId, Pageable pageable);

    Page<OrderVisits> findAllOrderVisitsWithSearch(String brandId, String searchTerm, Pageable pageable);
}
