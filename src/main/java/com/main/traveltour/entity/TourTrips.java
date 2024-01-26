package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(name = "tour_id", nullable = false, length = 30)
    private String tourId;

    @Basic
    @Column(name = "day_in_trip")
    private Integer dayInTrip;

    @Lob
    @Column(name = "activity_in_day", columnDefinition = "LONGTEXT")
    private String activityInDay;

    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Tours toursByTourId;
}
