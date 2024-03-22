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
@Table(name = "transportation_brands", schema = "travel_tour")
public class TransportationBrands {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "transportation_brand_name")
    private String transportationBrandName;

    @Basic
    @Column(name = "agencies_id")
    private int agenciesId;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @Basic
    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "transportation_brand_address")
    private String transportationBrandAddress;

    @Basic
    @Column(name = "transportation_brand_img")
    private String transportationBrandImg;

    @Basic
    @Column(name = "transportation_brand_policy", columnDefinition = "LONGTEXT")
    private String transportationBrandPolicy;

    @OneToMany(mappedBy = "transportationBrandsByTransportationBrandId")
    @JsonManagedReference
    private Collection<Transportations> transportationsById;

    @ManyToOne
    @JoinColumn(name = "agencies_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Agencies agenciesByAgenciesId;
}
