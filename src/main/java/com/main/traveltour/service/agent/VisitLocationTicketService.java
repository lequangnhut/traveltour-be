package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocationTickets;

import java.util.List;

public interface VisitLocationTicketService {

    String findMaxCode();

    List<VisitLocationTickets> findByVisitLocationId(String visitLocationId);

    VisitLocationTickets save(VisitLocationTickets visitLocationTickets);

    void delete(VisitLocationTickets visitLocationTickets);
}
