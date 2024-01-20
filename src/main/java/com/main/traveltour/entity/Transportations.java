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
@Table(name = "transportations", schema = "travel_tour")
public class Transportations {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "transportation_brand_id")
    private Integer transportationBrandId;

    @Basic
    @Column(name = "transportation_type_id")
    private Integer transportationTypeId;

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

    @OneToMany(mappedBy = "transportationsByTransportationId")
    @JsonManagedReference
    private Collection<TransportationSchedules> transportationSchedulesById;

    @ManyToOne
    @JoinColumn(name = "transportation_brand_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationBrands transportationBrandsByTransportationBrandId;

    @ManyToOne
    @JoinColumn(name = "transportation_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationTypes transportationTypesByTransportationTypeId;
}
