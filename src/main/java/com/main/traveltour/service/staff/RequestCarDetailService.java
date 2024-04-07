package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RequestCarDetailService {

    Page<RequestCarDetail> findAllRequestCarDetailPage(Integer requestCarId, Pageable pageable);

    Optional<RequestCarDetail> findRequestCarDetailById(Integer requestCarDetailId);

    List<RequestCarDetail> findAllRequestCarDetailList();

    RequestCarDetail findTimeRequestCarSubmitted(Integer requestCarId, String transportationId);

    RequestCarDetail save(RequestCarDetail requestCarDetail);
}
