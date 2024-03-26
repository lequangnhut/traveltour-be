package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "order_transportations", schema = "travel_tour")
public class OrderTransportations {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "transportation_schedule_id", nullable = false, length = 30)
    private String transportationScheduleId;

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
    @Column(name = "amount_ticket")
    private Integer amountTicket;

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
    @Column(name = "order_status")
    private Integer orderStatus; // 0: chờ thanh toán | 1: đã thanh toán | 2: thất bại

    @Basic
    @Column(name = "order_note")
    private String orderNote;

    @OneToMany(mappedBy = "orderTransportationById")
    @JsonManagedReference
    private Collection<OrderTransportationDetails> orderTransportationDetailById;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByUserId;

    @ManyToOne
    @JoinColumn(name = "transportation_schedule_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationSchedules transportationSchedulesByTransportationScheduleId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booking_tour_transportations", joinColumns = {@JoinColumn(name = "order_transportation_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "tour_detail_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("orderTransportations")
    private List<TourDetails> tourDetails = new ArrayList<>();
}
