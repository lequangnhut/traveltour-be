package com.main.traveltour.dto.agent.hotel.order;

import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.PaymentMethod;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderHotelDto {
        private String id;
        private Integer userId;
        private String customerName;
        private String customerCitizenCard;
        private String customerPhone;
        private String customerEmail;
        private Integer capacityAdult;
        private Integer capacityKid;
        private Timestamp checkIn;
        private Timestamp checkOut;
        private BigDecimal orderTotal;
        private String paymentMethod;
        private String orderCode;
        private Timestamp dateCreated;
        private Integer orderStatus;
        private String orderNote;
        private Collection<OrderHotelDetails> orderHotelDetailsById;
        private Users usersByUserId;
        private PaymentMethod orderByPaymentMethod;
        private List<TourDetails> tourDetails = new ArrayList<>();

}
