package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TourDetailsService {
    List<TourDetails> findAll();

    Page<TourDetails> findAll(Pageable pageable);

    Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable);

    Optional<TourDetails> findById(String id);

    TourDetails save(TourDetails tourDetails);

    void deleteById(String id);
}
