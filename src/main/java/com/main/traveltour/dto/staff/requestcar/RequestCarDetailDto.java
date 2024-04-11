package com.main.traveltour.dto.staff.requestcar;

import com.main.traveltour.entity.RequestCarDetail;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RequestCarDetail}
 */
@Data
public class RequestCarDetailDto implements Serializable {

    Integer id;

    Integer requestCarId;

    String transportationScheduleId;

    Timestamp dateCreated;

    Integer isAccepted;
}