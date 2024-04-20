package com.main.traveltour.service.staff;

import com.main.traveltour.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookingTourTransportationService {

    Page<TransportationSchedules> findTransportationSchedulesByTourDetailId
            (String tourDetailId, Integer orderStatus, String searchTerm, Pageable pageable);

    void update(OrderTransportations orderTransportations);

    Page<TransportationSchedules> findTransportationSchedulesByTourDetailIdForGuide
            (String tourDetailId, String searchTerm, Pageable pageable);

}
