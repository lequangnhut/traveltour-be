package com.main.traveltour.dto.customer.infomation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TourTypes;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TourCusDto {
    private String id;
    private Integer tourTypeId;
    private String tourName;
    private String tourImg;
    private Timestamp dateCreated;
    private Timestamp dateDeleted;
    private Boolean isActive;
    private String tourDescription;
}
