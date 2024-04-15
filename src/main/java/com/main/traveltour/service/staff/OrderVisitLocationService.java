package com.main.traveltour.service.staff;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.OrderVisits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderVisitLocationService {
    String maxCode();

    OrderVisits save(OrderVisits orderVisits);

    Page<OrderVisits> findByUserIdAndStatus(Integer orderStatus, String email, Pageable pageable);

    OrderVisits findById(String id);

}
