package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "transportations", schema = "travel_tour")
public class Transportations {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "transportation_brand_id", nullable = false, length = 30)
    private String transportationBrandId;

    @Basic
    @Column(name = "transportation_type_id")
    private Integer transportationTypeId;

    @Basic
    @Column(name = "transportation_img")
    private String transportationImg;

    @Basic
    @Column(name = "license_plate")
    private String licensePlate;

    @Basic
    @Column(name = "amount_seat")
    private Integer amountSeat;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "is_transport_bed")
    private Boolean isTransportBed; // false là xe ghế | true là xe giường

    @OneToMany(mappedBy = "transportationsByTransportationId")
    @JsonManagedReference
    private Collection<TransportationSchedules> transportationSchedulesById;

    @OneToMany(mappedBy = "transportationsByTransportationId")
    @JsonManagedReference
    private Collection<TransportationImage> transportationImagesById;

    @ManyToOne
    @JoinColumn(name = "transportation_brand_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationBrands transportationBrandsByTransportationBrandId;

    @ManyToOne
    @JoinColumn(name = "transportation_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationTypes transportationTypesByTransportationTypeId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "transport_add_utilities", joinColumns = {@JoinColumn(name = "transportation_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "transportation_utilities_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("transportUtilities")
    private List<TransportUtilities> transportUtilities = new ArrayList<>();
}
