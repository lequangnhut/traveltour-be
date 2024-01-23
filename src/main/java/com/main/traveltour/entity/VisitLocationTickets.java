package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

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
    private Integer id;

    @Basic
    @Column(name = "visit_location_id", nullable = false, length = 30)
    private String visitLocationId;

    @Basic
    @Column(name = "ticket_type_name")
    private String ticketTypeName;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @OneToMany(mappedBy = "visitLocationTicketsByVisitLocationTicketId")
    @JsonManagedReference
    private Collection<OrderVisitDetails> orderVisitDetailsById;

    @ManyToOne
    @JoinColumn(name = "visit_location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private VisitLocations visitLocationsByVisitLocationId;
}
