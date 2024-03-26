package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit_locations", schema = "travel_tour")
public class VisitLocations {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

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
    @Column(name = "detail_description", columnDefinition = "LONGTEXT")
    private String detailDescription;

    @Basic
    @Column(name = "visit_location_type_id", nullable = false, length = 30)
    private int visitLocationTypeId;

    @Basic
    @Column(name = "agencies_id")
    private int agenciesId;

    @OneToMany(mappedBy = "visitLocationsByVisitLocationId")
    @JsonManagedReference
    private Collection<OrderVisits> orderVisitsById;

    @OneToMany(mappedBy = "visitLocationsByVisitLocationId")
    @JsonManagedReference
    private Collection<VisitLocationTickets> visitLocationTicketsById;

    @OneToMany(mappedBy = "visitLocationsByVisitLocationId")
    @JsonManagedReference
    private Collection<VisitLocationImage> visitLocationImagesById;

    @ManyToOne
    @JoinColumn(name = "visit_location_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private VisitLocationTypes visitLocationTypesByVisitLocationTypeId;

    @ManyToOne
    @JoinColumn(name = "agencies_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Agencies agenciesByAgenciesId;
}
