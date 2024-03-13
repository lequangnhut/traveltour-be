package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.Transportations;
import com.main.traveltour.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderTransportationsDto {
    String id;
    Integer userId;
    String transportationScheduleId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer amountTicket;
    BigDecimal orderTotal;
    Boolean paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
}