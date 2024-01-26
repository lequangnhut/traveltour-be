package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLocationImageRepository extends JpaRepository<VisitLocationImage, Integer> {
}