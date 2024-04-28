package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.repository.TourDetailsRepository;
import com.main.traveltour.service.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TourDetailsServiceImpl implements TourDetailsService {

    @Autowired
    private TourDetailsRepository repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getMaxCodeTourDetailId() {
        return repo.getMaxCodeTourDetailId();
    }

    @Override
    public List<TourDetails> findAll() {
        return repo.getAllTourDetail();
    }

    @Override
    public List<TourDetails> findAllOrderByBookingCountDesc() {
        return repo.findAllOrderByBookingCountDesc();
    }

    @Override
    public List<TourDetails> getAListOfPopularTours(String departureArrives, String departureFrom, Date departureDate, BigDecimal price, List<Integer> listOfIdsOfTourTypes) {
        return repo.getAListOfPopularTours(departureArrives, departureFrom, departureDate, price, listOfIdsOfTourTypes);
    }

    @Override
    public List<Integer> getListOfIdsOfTourTypes(String departureArrives) {
        return repo.getListOfIdsOfTourTypes(departureArrives);
    }

    @Override
    public List<TourDetails> getAllJoinBooking() {
        return repo.getAllJoinBooking();
    }

    @Override
    public List<TourDetails> findAllTourDetailUseRequestCar() {
        return repo.findAllTourDetailUseRequestCar();
    }

    @Override
    public Page<TourDetails> getAllTourDetailByStatusIs2AndSearchTerm(String searchTerm, Pageable pageable) {
        return repo.getAllTourDetailByStatusIs2AndSearchTerm(searchTerm, pageable);
    }

    @Override
    public List<Object[]> findTourTrend() {
        return repo.findTourDetailTrend();
    }

    @Override
    public Page<TourDetails> findAll(Pageable pageable) {
        return repo.findAllTourDetail(pageable);
    }

    @Override
    public Page<TourDetails> findAllTourDetailStaff(Integer tourDetailStatus, Pageable pageable) {
        return repo.findAllTourDetailStaff(tourDetailStatus, pageable);
    }

    @Override
    public Page<TourDetails> findTourDetailWithFilter(String departureArrives,
                                                      String departureFrom,
                                                      Integer numberOfPeople,
                                                      Date departureDate,
                                                      BigDecimal price,
                                                      List<Integer> tourTypesByTourTypeId,
                                                      Pageable pageable) {
        return repo.findTourDetailWithFilter(departureArrives, departureFrom, numberOfPeople, departureDate, price, tourTypesByTourTypeId, pageable);
    }

    @Override
    public Page<TourDetails> findTourDetailWithInFilter(List<String> tourDetailIdList, Integer numberOfPeople, Date departureDate, BigDecimal price, List<Integer> tourTypesByTourTypeId, Pageable pageable) {
        return repo.findTourDetailWithInFilter(tourDetailIdList, numberOfPeople, departureDate, price, tourTypesByTourTypeId, pageable);
    }

    @Override
    public List<String> findTourDetailIdList(String cleanArrives, String cleanFrom, List<String> tourDetailIdList) {
        return repo.findTourDetailIdList(cleanArrives, cleanFrom, tourDetailIdList);
    }

    @Override
    public Page<TourDetails> findAllTourDetailWithSearchStaff(Integer tourDetailStatus, String searchTerm, Pageable pageable) {
        return repo.findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(tourDetailStatus, searchTerm, pageable);
    }

    @Override
    public TourDetails findById(String id) {
        return repo.findById(id);
    }

    @Override
    public TourDetails getById(String id) {
        return repo.getById(id);
    }

    @Override
    public TourDetails save(TourDetails tourDetails) {
        return repo.save(tourDetails);
    }

    @Override
    public void delete(TourDetails tourDetails) {
        repo.delete(tourDetails);
    }

    @Override
    public void updateStatusAndActive() {
        List<TourDetails> tourDetails1 = repo.findTourInProgress();
        List<TourDetails> tourDetails2 = repo.findTourCompleted();

        for (TourDetails tourDetails : tourDetails1) {
            tourDetails.setTourDetailStatus(2);
            saveTourDetail(tourDetails);
        }
        for (TourDetails tourDetails : tourDetails2) {
            tourDetails.setTourDetailStatus(3);
            saveTourDetail(tourDetails);
        }
    }

    public void saveTourDetail(TourDetails tourDetails) {
        String sql = "UPDATE tour_details SET tour_detail_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, tourDetails.getTourDetailStatus(), tourDetails.getId());
    }

    @Override
    public Long countTourDetails() {
        return repo.countTourDetails();
    }

    @Override
    public Page<TourDetails> findTourGuide(Integer guideId, Integer tourStatus, String searchTerm, Pageable pageable) {
        return repo.findTourByGuideAndStatus(guideId, tourStatus, searchTerm, pageable);
    }
}
