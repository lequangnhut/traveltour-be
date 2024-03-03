package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TourDetailsService {

    String getMaxCodeTourDetailId();

    List<TourDetails> findAll();

    Page<TourDetails> findAll(Pageable pageable);

    Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable);

    Optional<TourDetails> findById(String id);

    TourDetails getById(String id);

    TourDetails save(TourDetails tourDetails);

    void delete(TourDetails tourDetails);

    void updateStatusAndActive();
}
