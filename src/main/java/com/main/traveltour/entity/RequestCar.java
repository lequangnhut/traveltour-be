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
@Table(name = "request_car", schema = "travel_tour")
public class RequestCar {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "tour_detail_id", nullable = false, length = 30)
    private String tourDetailId;

    @Basic
    @Column(name = "amount_customer")
    private Integer amountCustomer;

    @Basic
    @Column(name = "departure_date")
    private Timestamp departureDate;

    @Basic
    @Column(name = "arrival_date")
    private Timestamp arrivalDate;

    @Basic
    @Column(name = "from_location")
    private String fromLocation;

    @Basic
    @Column(name = "to_location")
    private String toLocation;

    @Basic
    @Column(name = "is_transport_bed")
    private Boolean isTransportBed; // false là xe ghế | true là xe giường

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_accepted")
    private Timestamp dateAccepted;

    @Basic
    @Column(name = "is_accepted")
    private Boolean isAccepted; // false là yêu cầu đang chờ duyệt xe | true là yêu cầu đã duyệt xe

    @Basic
    @Column(name = "is_active")
    private Boolean isActive; // true là yêu cầu đang đăng | false là ẩn yêu cầu

    @Basic
    @Column(name = "request_car_noted", columnDefinition = "LONGTEXT")
    private String requestCarNoted;

    @ManyToOne
    @JoinColumn(name = "tour_detail_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TourDetails tourDetailsByTourDetailId;

    @OneToMany(mappedBy = "requestCarRequireCarById")
    @JsonManagedReference
    private Collection<RequestCarDetail> requestCarDetailsById;
}
