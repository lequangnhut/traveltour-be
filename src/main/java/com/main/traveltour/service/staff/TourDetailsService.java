package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TourDetailsService {

    String getMaxCodeTourDetailId();

    List<TourDetails> findAll();

    List<TourDetails> findAllOrderByBookingCountDesc();

    List<TourDetails> getAListOfPopularTours(String departureArrives, String departureFrom, Date departureDate, BigDecimal price, List<Integer> listOfIdsOfTourTypes);

    List<Integer> getListOfIdsOfTourTypes(String departureArrives);

    List<TourDetails> getAllJoinBooking();

    List<TourDetails> findAllTourDetailUseRequestCar();

    Page<TourDetails> getAllTourDetailByStatusIs2AndSearchTerm(String searchTerm, Pageable pageable);

    List<Object[]> findTourTrend();

    Page<TourDetails> findAll(Pageable pageable);

    Page<TourDetails> findTourDetailWithFilter(String departureArrives,
                                               String departureFrom,
                                               Integer numberOfPeople,
                                               Date departureDate,
                                               BigDecimal price,
                                               List<Integer> tourTypesByTourTypeId,
                                               Pageable pageable);

    Page<TourDetails> findTourDetailWithInFilter(List<String> tourDetailIdList,
                                                 Integer numberOfPeople,
                                                 Date departureDate,
                                                 BigDecimal price,
                                                 List<Integer> tourTypesByTourTypeId,
                                                 Pageable pageable);

    List<String> findTourDetailIdList(String cleanArrives, String cleanFrom, List<String> tourDetailIdList);

    Page<TourDetails> findAllTourDetailStaff(Integer tourDetailStatus, Pageable pageable);

    Page<TourDetails> findAllTourDetailWithSearchStaff(Integer tourDetailStatus, String searchTerm, Pageable pageable);

    TourDetails findById(String id);

    TourDetails getById(String id);

    TourDetails save(TourDetails tourDetails);

    void delete(TourDetails tourDetails);

    void updateStatusAndActive();

    Long countTourDetails();

    Page<TourDetails> findTourGuide(Integer guideId, Integer tourStatus, String searchTerm, Pageable pageable);
}
