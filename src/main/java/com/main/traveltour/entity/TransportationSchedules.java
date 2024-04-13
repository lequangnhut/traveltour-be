package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportation_schedules", schema = "travel_tour")
public class TransportationSchedules {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "transportation_id", nullable = false, length = 30)
    private String transportationId;

    @Basic
    @Column(name = "from_location")
    private String fromLocation;

    @Basic
    @Column(name = "to_location")
    private String toLocation;

    @Basic
    @Column(name = "from_address")
    private String fromAddress;

    @Basic
    @Column(name = "to_address")
    private String toAddress;

    @Basic
    @Column(name = "departure_time")
    private Timestamp departureTime;

    @Basic
    @Column(name = "arrival_time")
    private Timestamp arrivalTime;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "booked_seat")
    private Integer bookedSeat;

    @Basic
    @Column(name = "trip_type")
    private Boolean tripType; // true là hợp đồng, false là tuyến cố định

    @Basic
    @Column(name = "is_status")
    private Integer isStatus; // 0 chờ vận hành | 1 đang vận hành | 2 đã hoàn tất | 3 hủy chuyến đi | Tuyến cố định
                              // 4 chuyến đi đang trống | 5 đang chờ duyệt | 6 Đã được duyệt | 7 Từ chối duyệt
    @Basic
    @Column(name = "schedule_note")
    private String scheduleNote;

    @OneToMany(mappedBy = "transportationSchedulesByTransportationScheduleId")
    @JsonManagedReference
    private Collection<OrderTransportations> orderTransportationsById;

    @OneToMany(mappedBy = "transportationSchedulesByTransportationScheduleId")
    @JsonManagedReference
    private Collection<RequestCarDetail> requestCarDetailsById;

    @OneToMany(mappedBy = "transportationSchedulesByTransportationScheduleId")
    @JsonManagedReference
    private Collection<TransportationScheduleSeats> transportationScheduleSeatsById;

    @ManyToOne
    @JoinColumn(name = "transportation_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Transportations transportationsByTransportationId;
}
