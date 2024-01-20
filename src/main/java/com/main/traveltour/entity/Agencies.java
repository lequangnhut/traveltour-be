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
@Table(name = "agencies", schema = "travel_tour")
public class Agencies {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name_agency")
    private String nameAgency;

    @Basic
    @Column(name = "representative_name")
    private String representativeName;

    @Basic
    @Column(name = "tax_id")
    private String taxId;

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
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "is_accepted")
    private Integer isAccepted;

    @Basic
    @Column(name = "img_document")
    private String imgDocument;

    @OneToMany(mappedBy = "agenciesByAgenciesId")
    @JsonManagedReference
    private Collection<Hotels> hotelsById;

    @OneToMany(mappedBy = "agenciesByAgenciesId")
    @JsonManagedReference
    private Collection<TransportationBrands> transportationBrandsById;

    @OneToMany(mappedBy = "agenciesByAgenciesId")
    @JsonManagedReference
    private Collection<VisitLocations> visitLocationsById;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Users usersByUserId;
}
