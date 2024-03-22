package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderTransportationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTransportationDetailsRepository extends JpaRepository<OrderTransportationDetails, Integer> {
}