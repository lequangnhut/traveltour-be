package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_tour_customers", schema = "travel_tour")
public class BookingTourCustomers {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "booking_tour_id")
    private Integer bookingTourId;

    @Basic
    @Column(name = "customer_name")
    private String customerName;

    @Basic
    @Column(name = "customer_birth")
    private Date customerBirth;

    @Basic
    @Column(name = "customer_phone")
    private String customerPhone;
}
