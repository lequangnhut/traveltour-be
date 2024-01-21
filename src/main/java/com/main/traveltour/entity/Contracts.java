package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contracts", schema = "travel_tour")
public class Contracts {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "booking_tour_id", nullable = false, length = 30)
    private String bookingTourId;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @ManyToOne
    @JoinColumn(name = "booking_tour_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private BookingTours bookingToursByBookingTourId;
}
