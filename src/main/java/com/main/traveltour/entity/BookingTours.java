package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_tours", schema = "travel_tour")
public class BookingTours {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "tour_detail_id", nullable = false, length = 30)
    private String tourDetailId;

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
    @Column(name = "capacity_baby")
    private Integer capacityBaby;

    @Basic
    @Column(name = "order_total")
    private BigDecimal orderTotal;

    @Basic
    @Column(name = "payment_method")
    private Integer paymentMethod; // 0: travel | 1: VNPay | 2: ZaLoPay | 3: Momo

    @Basic
    @Column(name = "order_code")
    private String orderCode;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_cancelled")
    private Timestamp dateCancelled;

    @Basic
    @Column(name = "order_status")
    private Integer orderStatus; // 0: chờ thanh toán | 1: đã thanh toán | 2: thất bại

    @Basic
    @Column(name = "order_note")
    private String orderNote;

    @OneToMany(mappedBy = "bookingToursByBookingTourId")
    @JsonManagedReference
    private Collection<BookingTourCustomers> bookingTourCustomersById;

    @OneToMany(mappedBy = "bookingToursByBookingTourId")
    @JsonManagedReference
    private Collection<Contracts> contractsById;

    @OneToMany(mappedBy = "bookingToursByBookingTourId")
    @JsonManagedReference
    private Collection<Invoices> invoicesById;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByUserId;

    @ManyToOne
    @JoinColumn(name = "tour_detail_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TourDetails tourDetailsByTourDetailId;
}
