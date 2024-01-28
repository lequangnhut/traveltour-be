package com.main.traveltour.service.agent;

import com.main.traveltour.entity.OrderTransportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderTransportService {

    String findMaxCode();

    OrderTransportations findById(String id);

    OrderTransportations save(OrderTransportations orderTransportations);

    Page<OrderTransportations> findAllOrderTransport(String transportBrandId, Pageable pageable);

    Page<OrderTransportations> findAllOrderTransportWithSearch(String transportBrandId, String searchTerm, Pageable pageable);
}
