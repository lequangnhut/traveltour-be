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
@Table(name = "order_visit_details", schema = "travel_tour")
public class OrderVisitDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "order_visit_id")
    private Integer orderVisitId;

    @Basic
    @Column(name = "visit_location_ticket_id")
    private Integer visitLocationTicketId;

    @Basic
    @Column(name = "amount")
    private Integer amount;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
