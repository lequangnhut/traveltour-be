package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocationTickets;

public interface VisitLocationTicketService {

    String findMaxCode();

    VisitLocationTickets save(VisitLocationTickets visitLocationTickets);
}
