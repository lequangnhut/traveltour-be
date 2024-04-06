package com.main.traveltour.repository;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCarDetailRepository extends JpaRepository<RequestCarDetail, Integer> {

    Page<RequestCarDetail> findAllByRequestCarId(Integer requestCarId, Pageable pageable);

    RequestCarDetail findByRequestCarIdAndTransportationId(Integer requestCarDetail, String transportationId);
}