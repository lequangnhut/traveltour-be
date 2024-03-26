package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "order_visits", schema = "travel_tour")
public class OrderVisits {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "visit_location_id", nullable = false, length = 30)
    private String visitLocationId;

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
    private Integer paymentMethod; //0: vày, 1: vnp, 2: momo, 3: zalo

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booking_tour_visits", joinColumns = {@JoinColumn(name = "order_visit_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "tour_detail_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("orderVisits")
    private List<TourDetails> tourDetails = new ArrayList<>();

    @OneToMany(mappedBy = "orderVisitsByOrderVisitId")
    private Collection<OrderVisitDetails> orderVisitDetailsById;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByUserId;

    @ManyToOne
    @JoinColumn(name = "visit_location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private VisitLocations visitLocationsByVisitLocationId;
}
