package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "tour_id")
    private Integer tourId;

    @Basic
    @Column(name = "day_in_trip")
    private Integer dayInTrip;

    @Basic
    @Column(name = "activity_in_day")
    private String activityInDay;
}
