package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocationTickets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitLocationTicketService {

    String findMaxCode();

    VisitLocationTickets findByVisitTicketId(int visitTicketId);

    VisitLocationTickets findByTicketTypeNameAndLocationId(String ticketTypeName, String locationId);

    List<VisitLocationTickets> findByVisitLocationId(String visitLocationId);

    Page<VisitLocationTickets> findAllVisitTickets(String brandId, Pageable pageable);

    Page<VisitLocationTickets> findAllWithSearchVisitTickets(String brandId, String searchTerm, Pageable pageable);

    VisitLocationTickets save(VisitLocationTickets visitLocationTickets);

    void delete(VisitLocationTickets visitLocationTickets);
}
