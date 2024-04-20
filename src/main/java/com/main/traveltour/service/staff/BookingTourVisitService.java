package com.main.traveltour.service.staff;

import com.main.traveltour.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookingTourVisitService {

    Page<VisitLocations> findVisitByTourDetailId(String tourDetailId,
                                                 Integer orderVisitStatus,
                                                 String searchTerm,
                                                 Pageable pageable);

    List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitId(String tourDetailId,
                                                                         String visitId,
                                                                         Integer orderVisitStatus);

    List<OrderVisits> findOrderVisitByTourDetailIdAndVisitId(String tourDetailId,
                                                             String visitId,
                                                             Integer orderVisitStatus);

    void update(OrderVisits orderVisits);

    Page<VisitLocations> findVisitByTourDetailIdGuide(String tourDetailId, String searchTerm, Pageable pageable);

    List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitIdGuide(String tourDetailId, String visitId);
}