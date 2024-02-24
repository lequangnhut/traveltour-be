package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDestinations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourDestinationsRepository extends JpaRepository<TourDestinations, Integer> {

    List<TourDestinations> findAllByTourDetailId(String tourDetailId);

    TourDestinations findByTourDetailId(String tourDetailId);
}