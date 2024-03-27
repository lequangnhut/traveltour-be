package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportation_schedule_seats", schema = "travel_tour")
public class TransportationScheduleSeats {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "delay_booking")
    private Timestamp delayBooking;

    @Basic
    @Column(name = "transportation_schedule_id", nullable = false, length = 30)
    private String transportationScheduleId;

    @OneToMany(mappedBy = "transportationScheduleSeatById")
    @JsonManagedReference
    private Collection<OrderTransportationDetails> orderTransportationDetailById;

    @ManyToOne
    @JoinColumn(name = "transportation_schedule_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationSchedules transportationSchedulesByTransportationScheduleId;
}
