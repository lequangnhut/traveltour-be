package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_car_detail", schema = "travel_tour")
public class RequestCarDetail {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "request_car_id", nullable = false)
    private Integer requestCarId;

    @Basic
    @Column(name = "transportation_schedule_id", nullable = false, length = 30)
    private String transportationScheduleId;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_accepted")
    private Integer isAccepted; // 0 đã nộp | 1 đã duyệt | 2 hủy yêu cầu | 3 xe không được duyệt

    @ManyToOne
    @JoinColumn(name = "transportation_schedule_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TransportationSchedules transportationSchedulesByTransportationScheduleId;

    @ManyToOne
    @JoinColumn(name = "request_car_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private RequestCar requestCarRequireCarById;
}
