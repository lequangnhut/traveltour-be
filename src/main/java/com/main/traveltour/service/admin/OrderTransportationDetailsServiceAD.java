package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.OrderTransportationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderTransportationDetailsServiceAD {

    List<OrderTransportationDetails> findByOrderId(String id) ;


}
