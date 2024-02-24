package com.main.traveltour.dto.admin;

import com.main.traveltour.entity.Agencies;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Notifications}
 */
@Data
public class NotificationsDtoAD implements Serializable {

    int id;

    int agenciesId;

    String statusMessage;

    Timestamp dateCreated;

    Boolean isSeen;

    Agencies agenciesByAgenciesId;
}