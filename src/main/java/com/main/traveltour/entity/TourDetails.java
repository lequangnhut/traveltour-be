package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_details", schema = "travel_tour")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    private Timestamp departureDate;

    @Basic
    @Column(name = "arrival_date")
    private Timestamp arrivalDate;

    @Basic
    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Basic
    @Column(name = "minimum_number_of_guests")
    private Integer minimumNumberOfGuests;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic
    @Column(name = "tour_detail_notes")
    private String tourDetailNotes;

    @Basic
    @Column(name = "tour_detail_status")
    private Integer tourDetailStatus; // 1: chờ vận hành | 2: đang vận hành | 3: đã hoàn thành | 4: đã hủy

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @Basic
    @Column(name = "tour_detail_description", columnDefinition = "LONGTEXT")
    private String tourDetailDescription;

    @Basic
    @Column(name = "booked_seat")
    private Integer bookedSeat;

    @Basic
    @Column(name = "from_location")
    private String fromLocation;

    @Basic
    @Column(name = "to_location")
    private String toLocation;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<BookingTours> bookingToursById;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<TourDetailImages> tourDetailImagesById;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<TourDestinations> tourDestinationsById;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<TourTrips> tourTripsById;

    @OneToMany(mappedBy = "tourDetailsByTourDetailId")
    @JsonManagedReference
    private Collection<RequestCar> requestCarsById;

    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Tours toursByTourId;

    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Users usersByGuideId;

    @ManyToMany(mappedBy = "tourDetails")
    private List<OrderHotels> orderHotels;

    @ManyToMany(mappedBy = "tourDetails")
    private List<OrderTransportations> orderTransportations;

    @ManyToMany(mappedBy = "tourDetails")
    private List<OrderVisits> orderVisits;
}
