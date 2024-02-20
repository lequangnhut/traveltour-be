package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetailImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourDetailImagesRepository extends JpaRepository<TourDetailImages, Integer> {

    List<TourDetailImages> findAllByTourDetailId(String tourDetailId);
}