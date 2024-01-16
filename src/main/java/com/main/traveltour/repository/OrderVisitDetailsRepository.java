package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderVisitDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderVisitDetailsRepository extends JpaRepository<OrderVisitDetails, Integer> {
}