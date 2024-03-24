package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderTransportationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTransportationDetailsRepository extends JpaRepository<OrderTransportationDetails, Integer> {

    @Query("SELECT otd FROM OrderTransportationDetails otd WHERE otd.OrderTransportationId = :id")
    List<OrderTransportationDetails> findALlByOrderId(String id);

}