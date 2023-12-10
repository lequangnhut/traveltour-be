package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_visits", schema = "travel_tour")
public class OrderVisits {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "visit_location_id")
    private Integer visitLocationId;

    @Basic
    @Column(name = "customer_name")
    private String customerName;

    @Basic
    @Column(name = "customer_citizen_card")
    private String customerCitizenCard;

    @Basic
    @Column(name = "customer_phone")
    private String customerPhone;

    @Basic
    @Column(name = "customer_email")
    private String customerEmail;

    @Basic
    @Column(name = "capacity_adult")
    private Integer capacityAdult;

    @Basic
    @Column(name = "capacity_kid")
    private Integer capacityKid;

    @Basic
    @Column(name = "check_in")
    private Timestamp checkIn;

    @Basic
    @Column(name = "order_total")
    private BigDecimal orderTotal;

    @Basic
    @Column(name = "payment_method")
    private Boolean paymentMethod;

    @Basic
    @Column(name = "order_code")
    private String orderCode;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "order_status")
    private Integer orderStatus;

    @Basic
    @Column(name = "order_note")
    private String orderNote;

    @ManyToMany(mappedBy = "orderVisits")
    private List<BookingTours> bookingTours;
}
