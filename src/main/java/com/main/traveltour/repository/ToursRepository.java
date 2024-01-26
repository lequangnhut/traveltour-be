package com.main.traveltour.repository;

import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Integer> {

    Optional<Tours> findById(String tourId);

    @Query("SELECT t.tourName FROM Tours t WHERE t.id = :tourId")
    String getToursNameByTourId(@Param("tourId") String tourId);

    List<Tours> findAllByIsActiveIsTrue();

    Page<Tours> findAllByIsActiveIsTrue(Pageable pageable);

    // Phương thức tìm kiếm dựa trên tên tour
    Page<Tours> findByTourNameContainingIgnoreCaseAndIsActiveIsTrue(String searchTerm, Pageable pageable);

    @Query("SELECT COALESCE(MAX(t.id), 'TR0000') FROM Tours t")
    String maxCodeTourId();

    Page<Tours> findByTourNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    List<Tours> findAllByTourTypeId(int id);
}