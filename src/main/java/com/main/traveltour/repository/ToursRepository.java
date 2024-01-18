package com.main.traveltour.repository;

import com.main.traveltour.entity.Tours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Integer> {
}