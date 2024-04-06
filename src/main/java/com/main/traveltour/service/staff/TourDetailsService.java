package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TourDetailsService {

    String getMaxCodeTourDetailId();

    List<TourDetails> findAll();

    List<TourDetails> getAllJoinBooking();

    List<TourDetails> findAllTourDetailUseRequestCar();

    Page<TourDetails> getAllTourDetailByStatusIs2AndSearchTerm(String searchTerm, Pageable pageable);

    List<Object[]> findTourTrend();

    Page<TourDetails> findAll(Pageable pageable);

    Page<TourDetails> findTourDetailWithFilter(String searchTerm,
                                               Date departureDate,
                                               BigDecimal price,
                                               List<Integer> tourTypesByTourTypeId,
                                               Pageable pageable);

    Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable);

    TourDetails findById(String id);

    TourDetails getById(String id);

    TourDetails save(TourDetails tourDetails);

    void delete(TourDetails tourDetails);

    void updateStatusAndActive();

    Long countTourDetails();
}
