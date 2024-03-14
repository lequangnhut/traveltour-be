package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.entity.VisitLocationTickets;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class OrderVisitDetailsGetDataDto {
    int id;
    String orderVisitId;
    Integer visitLocationTicketId;
    Integer amount;
    OrderVisits orderVisitsByOrderVisitId;
    VisitLocationTickets visitLocationTicketsByVisitLocationTicketId;
}