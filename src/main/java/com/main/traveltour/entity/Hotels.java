package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels", schema = "travel_tour")
public class Hotels {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "hotel_name")
    private String hotelName;

    @Basic
    @Column(name = "url_website")
    private String urlWebsite;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "amount_room")
    private Integer amountRoom;

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
    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "hotel_type_id")
    private int hotelTypeId;

    @Basic
    @Column(name = "agencies_id")
    private int agenciesId;

    @OneToMany(mappedBy = "hotelsByHotelId")
    private Collection<RoomTypes> roomTypesById;

    @OneToMany(mappedBy = "hotelsByHotelId")
    private Collection<HotelImages> hotelImagesById;

    @ManyToOne
    @JoinColumn(name = "hotel_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private HotelTypes hotelTypesByHotelTypeId;

    @ManyToOne
    @JoinColumn(name = "agencies_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Agencies agenciesByAgenciesId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "hotel_utilities", joinColumns = {@JoinColumn(name = "hotel_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "place_utilities_id", referencedColumnName = "id")})
    private List<PlaceUtilities> placeUtilities = new ArrayList<>();
}
