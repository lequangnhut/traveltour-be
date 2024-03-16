package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

public interface TourDetailsService {

    String getMaxCodeTourDetailId();

    List<TourDetails> findAll();

    List<TourDetails> getAllJoinBooking();

    List<Object[]> findTourTrend();

    Page<TourDetails> findAll(Pageable pageable);

    Page<TourDetails> findTourDetailWithFilter(String searchTerm,
                                               Date departureDate,
                                               BigDecimal price,
                                               List<Integer> tourTypesByTourTypeId,
                                               Pageable pageable)   ;

    Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable);

    TourDetails findById(String id);

    TourDetails getById(String id);

    TourDetails save(TourDetails tourDetails);

    void delete(TourDetails tourDetails);

    void updateStatusAndActive();
}
