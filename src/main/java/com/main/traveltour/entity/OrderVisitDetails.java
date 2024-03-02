package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_visit_details", schema = "travel_tour")
public class OrderVisitDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "order_visit_id", nullable = false, length = 30)
    private String orderVisitId;

    @Basic
    @Column(name = "visit_location_ticket_id")
    private Integer visitLocationTicketId;

    @Basic
    @Column(name = "amount")
    private Integer amount;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_visit_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private OrderVisits orderVisitsByOrderVisitId;

    @ManyToOne
    @JoinColumn(name = "visit_location_ticket_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private VisitLocationTickets visitLocationTicketsByVisitLocationTicketId;
}
