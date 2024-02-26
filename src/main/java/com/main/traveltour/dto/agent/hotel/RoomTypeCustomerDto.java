package com.main.traveltour.dto.agent.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.main.traveltour.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoomTypeCustomerDto {
    private Integer totalCount;
    private List<RoomTypes> roomTypes;
}
