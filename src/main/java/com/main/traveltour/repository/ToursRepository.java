package com.main.traveltour.repository;

import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Integer> {

    Optional<Tours> findById(String tourId);

    List<Tours> findAllByIsActiveIsTrue();

    Page<Tours> findAllByIsActiveIsTrue(Pageable pageable);

    // Phương thức tìm kiếm dựa trên tên tour
    Page<Tours> findByTourNameContainingIgnoreCaseAndIsActiveIsTrue(String searchTerm, Pageable pageable);

    @Query("SELECT COALESCE(MAX(t.id), 'TR0000') FROM Tours t")
    String maxCodeTourId();

}