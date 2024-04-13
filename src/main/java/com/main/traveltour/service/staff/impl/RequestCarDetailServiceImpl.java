package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.RequestCarDetail;
import com.main.traveltour.repository.RequestCarDetailRepository;
import com.main.traveltour.service.staff.RequestCarDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public Page<RequestCarDetail> findAllHistotyRequestCarPage(Integer acceptedRequest, String transportBrandId, Pageable pageable) {
        return repo.findAllHistoryRequestCar(acceptedRequest, transportBrandId, pageable);
    }

    @Override
    public void updateAll(String requestCarId) {
        List<RequestCarDetail> requestCarDetailList = repo.findAllByRequestCarId(Integer.parseInt(requestCarId));

        for (RequestCarDetail requestCarDetail : requestCarDetailList) {
            requestCarDetail.getTransportationSchedulesByTransportationScheduleId().setIsStatus(7); // 7 Từ chối duyệt
            requestCarDetail.setIsAccepted(3); // 3 xe không được duyệt
            requestCarDetail.setDateCreated(new Timestamp(System.currentTimeMillis()));
            repo.save(requestCarDetail);
        }
    }

    @Override
    public Optional<RequestCarDetail> findRequestCarDetailById(Integer requestCarDetailId) {
        return repo.findById(requestCarDetailId);
    }

    @Override
    public RequestCarDetail findRequestCarDetailSubmitted(String transportationScheduleId) {
        return repo.findRequestCarDetailSubmitted(transportationScheduleId);
    }

    @Override
    public RequestCarDetail findCarSubmitted(Integer requestCarId, String transportationScheduleId) {
        return repo.findCarSubmitted(requestCarId, transportationScheduleId);
    }

    @Override
    public RequestCarDetail save(RequestCarDetail requestCarDetail) {
        return repo.save(requestCarDetail);
    }
}
