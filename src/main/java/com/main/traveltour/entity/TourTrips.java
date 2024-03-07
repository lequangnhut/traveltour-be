package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_trips", schema = "travel_tour")
public class TourTrips {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "tour_detail_id", nullable = false, length = 30)
    private String tourDetailId;

    @Basic
    @Column(name = "transportation_type_id")
    private Integer transportationTypeId;

    @Basic
    @Column(name = "place_name")
    private String placeName;

    @Basic
    @Column(name = "place_image")
    private String placeImage;

    @Basic
    @Column(name = "place_address")
    private String placeAddress;

    @Basic
    @Column(name = "place_cost")
    private BigDecimal placeCost;

    @Basic
    @Column(name = "time_go")
    private Time timeGo;

    @Basic
    @Column(name = "activity_in_day")
    private String activityInDay;

    @Basic
    @Column(name = "day_in_trip")
    private Integer dayInTrip;

    @ManyToOne
    @JoinColumn(name = "tour_detail_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TourDetails tourDetailsByTourDetailId;

    @ManyToOne
    @JoinColumn(name = "transportation_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationTypes transportationTypesByTransportationTypeId;
}
