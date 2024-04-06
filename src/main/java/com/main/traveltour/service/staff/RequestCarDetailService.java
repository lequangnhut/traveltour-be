package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RequestCarDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestCarDetailService {

    Page<RequestCarDetail> findAllRequestCarDetailPage(Integer requestCarId, Pageable pageable);

    List<RequestCarDetail> findAllRequestCarDetailList();

    RequestCarDetail findTimeRequestCarSubmitted(Integer requestCarId, String transportationId);

    RequestCarDetail save(RequestCarDetail requestCarDetail);
}
