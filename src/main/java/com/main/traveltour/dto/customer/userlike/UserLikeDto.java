package com.main.traveltour.dto.customer.userlike;

import com.main.traveltour.dto.customer.infomation.HotelCusDto;
import com.main.traveltour.dto.customer.infomation.VisitLocationsCusDto;
import com.main.traveltour.entity.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLikeDto {
    private List<UserLikes> userLikes;
    private List<HotelCusDto> hotels;
    private List<Tours> tours;
    private List<TransportationBrands> transportationBrands;
    private List<VisitLocationsCusDto> visitLocations;

}
