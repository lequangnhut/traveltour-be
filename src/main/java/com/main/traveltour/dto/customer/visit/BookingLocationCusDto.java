package com.main.traveltour.dto.customer.visit;

import com.main.traveltour.dto.staff.OrderVisitsDto;
import com.main.traveltour.entity.OrderVisits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingLocationCusDto {
    OrderVisitsDto orderVisitsDto;
    OrderVisits orderVisits;
}
