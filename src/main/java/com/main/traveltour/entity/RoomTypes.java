package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "room_types", schema = "travel_tour")
public class RoomTypes {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "room_type_name")
    private String roomTypeName;

    @Basic
    @Column(name = "hotel_id", nullable = false, length = 30)
    private String hotelId;

    @Basic
    @Column(name = "capacity_adults")
    private Integer capacityAdults;

    @Basic
    @Column(name = "capacity_children")
    private Integer capacityChildren;

    @Basic
    @Column(name = "amount_room")
    private Integer amountRoom;

    @Basic
    @Column(name = "price")
    private BigDecimal price;

    @Basic
    @Column(name = "breakfast_included")
    private Boolean breakfastIncluded;

    @Basic
    @Column(name = "free_cancellation")
    private Boolean freeCancellation;

    @Basic
    @Column(name = "checkin_time")
    private LocalTime checkinTime;

    @Basic
    @Column(name = "checkout_time")
    private LocalTime checkoutTime;

    @Basic
    @Column(name = "is_active")
    private Integer isActive;

    @Basic
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @Basic
    @Column(name = "room_type_avatar")
    private String roomTypeAvatar;

    @Basic
    @Column(name = "room_type_description")
    private String roomTypeDescription;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    @ToString.Exclude
    private Collection<OrderHotelDetails> orderHotelDetailsById;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    @ToString.Exclude
    private Collection<RoomBeds> roomBedsById;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    @ToString.Exclude
    private Collection<RoomImages> roomImagesById;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Hotels hotelsByHotelId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "room_type_utilities", joinColumns = {@JoinColumn(name = "room_type_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "room_utilities_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("roomTypes")
    private List<RoomUtilities> roomUtilities = new ArrayList<>();
}
