package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportUtilitiesRepository extends JpaRepository<TransportUtilities, Integer> {

    TransportUtilities findById(int transUtilityId);

    Page<TransportUtilities> findByDescriptionContainingIgnoreCase(String searchTerm, Pageable pageable);
}