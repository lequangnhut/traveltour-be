package com.main.traveltour.dto.staff.requestcar;

import com.main.traveltour.entity.*;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RequestCarDetail}
 */
@Data
public class RequestCarDetailGetDataDto implements Serializable {

    Integer id;

    Integer requestCarId;

    String transportationScheduleId;

    Timestamp dateCreated;

    Integer isAccepted;

    Tours toursByTourId;

    Transportations transportations;

    TransportationBrands transportationBrands;

    TransportationSchedules transportationSchedules;

    RequestCar requestCar;

    TransportationTypes transportationTypes;
}