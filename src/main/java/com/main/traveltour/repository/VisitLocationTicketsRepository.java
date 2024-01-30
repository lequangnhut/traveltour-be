package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocationTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitLocationTicketsRepository extends JpaRepository<VisitLocationTickets, Integer> {

    @Query("SELECT MAX(tk.id) FROM VisitLocationTickets tk")
    String findMaxCode();

    List<VisitLocationTickets> findByVisitLocationId(String visitLocationId);
}