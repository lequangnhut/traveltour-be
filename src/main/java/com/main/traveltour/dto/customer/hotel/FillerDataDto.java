package com.main.traveltour.dto.customer.hotel;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FillerDataDto {
    BigDecimal priceFilter;
    List<Integer> hotelTypeIdListFilter;
    List<Integer> placeUtilitiesIdListFilter;
    List<Integer> roomUtilitiesIdListFilter;
    Boolean breakfastIncludedFilter;
    Boolean freeCancellationFilter;
    List<Integer> roomBedsIdListFilter;
    Integer amountRoomFilter;
    String locationFilter;
    Integer capacityAdultsFilter;
    Integer capacityChildrenFilter;
    String checkInDateFiller;
    String checkOutDateFiller;
    String hotelIdFilter;
    int page;
    int size;
    String sort;
}
