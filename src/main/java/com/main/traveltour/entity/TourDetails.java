package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_details", schema = "travel_tour")
public class TourDetails {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "tour_id", nullable = false, length = 30)
    private String tourId;

    @Basic
    @Column(name = "guide_id")
    private Integer guideId;

    @Basic
    @Column(name = "departure_date")
    private Date departureDate;

    @Basic
    @Column(name = "arrival_date")
    private Date arrivalDate;

    @Basic
    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic
    @Column(name = "tour_detail_notes")
    private String tourDetailNotes;

    @Basic
    @Column(name = "tour_detail_status")
    private Integer tourDetailStatus;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "tour_detail_description")
    private String tourDetailDescription;

    @Basic
    @Column(name = "from_location")
    private String from_location;

    @Basic
    @Column(name = "to_location")
    private String to_location;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<BookingTours> bookingToursById;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<TourDetailImages> tourDetailImagesById;

    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Tours toursByTourId;

    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByGuideId;
}
