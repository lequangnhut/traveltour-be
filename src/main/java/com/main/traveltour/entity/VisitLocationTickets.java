package com.main.traveltour.entity;

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
@Table(name = "visit_location_tickets", schema = "travel_tour")
public class VisitLocationTickets {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "visit_location_id")
    private Integer visitLocationId;

    @Basic
    @Column(name = "ticket_type_name")
    private String ticketTypeName;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
