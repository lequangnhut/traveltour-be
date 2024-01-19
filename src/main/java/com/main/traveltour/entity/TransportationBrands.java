package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportation_brands", schema = "travel_tour")
public class TransportationBrands {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "transportation_brand_name")
    private String transportationBrandName;

    @Basic
    @Column(name = "agencies_id")
    private int agenciesId;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "transportation_brand_img")
    private String transportationBrandImg;

    @Basic
    @Column(name = "transportation_brand_description")
    private String transportationBrandDescription;
}
