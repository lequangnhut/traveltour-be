package com.main.traveltour.entity;

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
@Table(name = "transportations", schema = "travel_tour")
public class Transportations {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "transportation_brand_id")
    private Integer transportationBrandId;

    @Basic
    @Column(name = "transportation_type_id")
    private Integer transportationTypeId;

    @Basic
    @Column(name = "license_plate")
    private String licensePlate;

    @Basic
    @Column(name = "amount_seat")
    private Integer amountSeat;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;
}
