package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RequestCarDetailService {

    RequestCarDetail findRequestCarDetailSubmitted(String transportationScheduleId);

    RequestCarDetail findCarSubmitted(Integer requestCarId, String transportationScheduleId);

    RequestCarDetail save(RequestCarDetail requestCarDetail);

    Optional<RequestCarDetail> findRequestCarDetailById(Integer requestCarDetailId);

    Page<RequestCarDetail> findAllRequestCarDetailPage(Integer requestCarId, Pageable pageable);

    Page<RequestCarDetail> findAllHistotyRequestCarPage(Integer acceptedRequest, String transportBrandId, Pageable pageable);

    void updateAll(String requestCarId);
}
