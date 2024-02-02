package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.VisitLocationTickets;
import com.main.traveltour.repository.VisitLocationTicketsRepository;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitLocationTicketServiceImpl implements VisitLocationTicketService {

    @Autowired
    private VisitLocationTicketsRepository visitLocationTicketsRepository;

    @Override
    public String findMaxCode() {
        return visitLocationTicketsRepository.findMaxCode();
    }

    @Override
    public VisitLocationTickets findByVisitTicketId(int visitTicketId) {
        return visitLocationTicketsRepository.findById(visitTicketId);
    }

    @Override
    public VisitLocationTickets findByTicketTypeNameAndLocationId(String ticketTypeName, String locationId) {
        return visitLocationTicketsRepository.findByTicketTypeNameAndVisitLocationId(ticketTypeName, locationId);
    }

    @Override
    public Page<VisitLocationTickets> findAllVisitTickets(String brandId, Pageable pageable) {
        return visitLocationTicketsRepository.findAllVisitTickets(brandId, pageable);
    }

    @Override
    public Page<VisitLocationTickets> findAllWithSearchVisitTickets(String brandId, String searchTerm, Pageable pageable) {
        return visitLocationTicketsRepository.findAllWithSearchVisitTickets(brandId, searchTerm, pageable);
    }

    @Override
    public List<VisitLocationTickets> findByVisitLocationId(String visitLocationId) {
        return visitLocationTicketsRepository.findByVisitLocationId(visitLocationId);
    }

    @Override
    public VisitLocationTickets save(VisitLocationTickets visitLocationTickets) {
        return visitLocationTicketsRepository.save(visitLocationTickets);
    }

    @Override
    public void delete(VisitLocationTickets visitLocationTickets) {
        visitLocationTicketsRepository.delete(visitLocationTickets);
    }
}
