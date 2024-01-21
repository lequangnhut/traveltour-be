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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booking_tour_hotels", joinColumns = {@JoinColumn(name = "booking_tour_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "order_hotel_id", referencedColumnName = "id")})
    private List<OrderHotels> orderHotels = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booking_tour_transportations", joinColumns = {@JoinColumn(name = "booking_tour_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "order_transportation_id", referencedColumnName = "id")})
    private List<OrderTransportations> orderTransportations = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booking_tour_visits", joinColumns = {@JoinColumn(name = "booking_tour_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "order_visit_id", referencedColumnName = "id")})
    private List<OrderVisits> orderVisits = new ArrayList<>();
}
