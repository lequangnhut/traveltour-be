package com.main.traveltour.repository;

import com.main.traveltour.entity.CancelOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelOrdersRepository extends JpaRepository<CancelOrders, Integer> {


}