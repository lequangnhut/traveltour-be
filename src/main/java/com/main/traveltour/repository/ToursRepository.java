package com.main.traveltour.repository;

import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Integer> {
    // Phương thức tìm kiếm dựa trên tên tour
    Page<Tours> findByTourNameContainingIgnoreCase(String searchTerm, Pageable pageable);
}