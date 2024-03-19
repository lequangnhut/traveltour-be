package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_transportations_details", schema = "travel_tour")
public class OrderTransportationDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "order_transportations_id", nullable = false, length = 30)
    private String OrderTransportationId;

    @Basic
    @Column(name = "transportations_schedule_seat_id")
    private Integer TransportationScheduleSeatId;

    @ManyToOne
    @JoinColumn(name = "order_transportations_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private OrderTransportations orderTransportationById;

    @ManyToOne
    @JoinColumn(name = "transportations_schedule_seat_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationScheduleSeats transportationScheduleSeatById;
}
