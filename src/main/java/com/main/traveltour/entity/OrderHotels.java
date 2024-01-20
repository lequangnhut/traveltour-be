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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_hotels", schema = "travel_tour")
public class OrderHotels {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

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
    @Column(name = "check_out")
    private Timestamp checkOut;

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

    @OneToMany(mappedBy = "orderHotelsByOrderHotelId")
    @JsonManagedReference
    private Collection<OrderHotelDetails> orderHotelDetailsById;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByUserId;

    @ManyToMany(mappedBy = "orderHotels")
    private List<BookingTours> bookingTours;
}
