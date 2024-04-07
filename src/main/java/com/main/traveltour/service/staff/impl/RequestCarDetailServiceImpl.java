package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.RequestCarDetail;
import com.main.traveltour.repository.RequestCarDetailRepository;
import com.main.traveltour.service.staff.RequestCarDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestCarDetailServiceImpl implements RequestCarDetailService {

    @Autowired
    private RequestCarDetailRepository repo;

    @Override
    public Page<RequestCarDetail> findAllRequestCarDetailPage(Integer requestCarId, Pageable pageable) {
        return repo.findAllByRequestCarId(requestCarId, pageable);
    }

    @Override
    public Optional<RequestCarDetail> findRequestCarDetailById(Integer requestCarDetailId) {
        return repo.findById(requestCarDetailId);
    }

    @Override
    public List<RequestCarDetail> findAllRequestCarDetailList() {
        return repo.findAll();
    }

    @Override
    public RequestCarDetail findTimeRequestCarSubmitted(Integer requestCarId, String transportationId) {
        return repo.findByRequestCarIdAndTransportationId(requestCarId, transportationId);
    }

    @Override
    public RequestCarDetail save(RequestCarDetail requestCarDetail) {
        return repo.save(requestCarDetail);
    }
}
