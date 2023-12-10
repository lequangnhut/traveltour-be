package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit_locations", schema = "travel_tour")
public class VisitLocations {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "visit_location_name")
    private String visitLocationName;

    @Basic
    @Column(name = "visit_location_image")
    private String visitLocationImage;

    @Basic
    @Column(name = "url_website")
    private String urlWebsite;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "province")
    private String province;

    @Basic
    @Column(name = "district")
    private String district;

    @Basic
    @Column(name = "ward")
    private String ward;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "opening_time")
    private Time openingTime;

    @Basic
    @Column(name = "closing_time")
    private Time closingTime;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "visit_location_type_id")
    private int visitLocationTypeId;

    @Basic
    @Column(name = "agencies_id")
    private int agenciesId;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;
}
