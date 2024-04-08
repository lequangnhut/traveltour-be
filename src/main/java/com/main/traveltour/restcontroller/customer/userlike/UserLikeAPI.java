package com.main.traveltour.restcontroller.customer.userlike;

import com.main.traveltour.dto.customer.infomation.HotelCusDto;
import com.main.traveltour.dto.customer.infomation.TourCusDto;
import com.main.traveltour.dto.customer.infomation.TransportationBrandsCusDto;
import com.main.traveltour.dto.customer.infomation.VisitLocationsCusDto;
import com.main.traveltour.dto.customer.userlike.UserLikeDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.customer.UserLikeService;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class UserLikeAPI {

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private ToursService toursService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @PostMapping("customer/user-like/saveLike")
    public ResponseObject saveLike(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("userId") Integer userId
    ) {
        // Kiểm tra dữ liệu đầu vào
        if (serviceId == null || categoryId == null || userId == null) {
            return new ResponseObject("400", "Dữ liệu đầu vào không hợp lệ", null);
        }

        if (!userLikeService.existsByServiceIdAndUserId(serviceId, userId)) {
            UserLikes userLikes = UserLikes.builder()
                    .category(categoryId)
                    .serviceId(serviceId)
                    .usersId(userId)
                    .usersByUserId(usersService.findById(userId))
                    .build();

            userLikeService.save(userLikes);
            // Xác nhận lưu trữ thành công trước khi trả về
            return switch (categoryId) {
                case 0 -> new ResponseObject("200", "Bạn đã thích tour này", true);
                case 1 -> new ResponseObject("200", "Bạn đã thích khách sạn này", true);
                case 2 -> new ResponseObject("200", "Bạn đã thích nhà xe này", true);
                case 3 -> new ResponseObject("200", "Bạn đã thích điểm du lịch này", true);
                default -> new ResponseObject("200", "Bạn đã thích dịch vụ này", true);
            };
        } else {
            userLikeService.deleteByServiceIdAndUsersId(serviceId, userId);

            // Xác nhận lưu trữ thành công trước khi trả về
            return switch (categoryId) {
                case 0 -> new ResponseObject("200", "Bạn đã bỏ thích tour này", false);
                case 1 -> new ResponseObject("200", "Bạn đã bỏ thích khách sạn này", false);
                case 2 -> new ResponseObject("200", "Bạn đã bỏ thích nhà xe này", false);
                case 3 -> new ResponseObject("200", "Bạn đã bỏ thích điểm du lịch này", false);
                default -> new ResponseObject("200", "Bạn đã bỏ thích dịch vụ này", false);
            };
        }

    }


    @GetMapping("customer/user-like/findUserLikeByCategoryIdAndServiceId")
    public ResponseObject findUserLikeByCategoryIdAndServiceId(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("userId") Integer userId
    ) {
        if (!userLikeService.existsByServiceIdAndUserId(serviceId, userId)) {
            return new ResponseObject("200", "OK", false);
        } else {
            return new ResponseObject("200", "OK", true);
        }
    }

    @GetMapping("customer/user-like/findAllUserLikeByUserId")
    public ResponseObject findAllUserLikeByUserId(
            @RequestParam("usersId") Integer userId
    ) {
        try {
            List<UserLikes> userLikes = userLikeService.findFavoritesList(userId);

            List<Tours> tours = userLikes.stream()
                    .filter(userLikeDto -> userLikeDto.getCategory() == 0)
                    .parallel()
                    .map(userLikeDto -> toursService.findById(userLikeDto.getServiceId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<Hotels> hotels = userLikes.stream()
                    .filter(userLikeDto -> userLikeDto.getCategory() == 1)
                    .parallel()
                    .map(userLikeDto -> hotelsService.findById(userLikeDto.getServiceId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<TransportationBrands> transportationBrands = userLikes.stream()
                    .filter(userLikeDto -> userLikeDto.getCategory() == 2)
                    .parallel()
                    .map(userLikeDto -> transportationBrandsService.findById(userLikeDto.getServiceId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<VisitLocations> visitLocations = userLikes.stream()
                    .filter(userLikeDto -> userLikeDto.getCategory() == 3)
                    .parallel()
                    .map(userLikeDto -> visitLocationsService.findById(userLikeDto.getServiceId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<HotelCusDto> hotelCusDto = EntityDtoUtils.convertToDtoList(hotels, HotelCusDto.class);
            List<VisitLocationsCusDto> visitLocationsCusDtos = EntityDtoUtils.convertToDtoList(visitLocations, VisitLocationsCusDto.class);
            List<TransportationBrandsCusDto> transportationBrandsCusDtos = EntityDtoUtils.convertToDtoList(transportationBrands, TransportationBrandsCusDto.class);
            List<TourCusDto> tourCusDtos = EntityDtoUtils.convertToDtoList(tours, TourCusDto.class);
            UserLikeDto userLikeDtos = UserLikeDto.builder()
                    .userLikes(userLikes)
                    .tours(tourCusDtos)
                    .hotels(hotelCusDto)
                    .transportationBrands(transportationBrandsCusDtos)
                    .visitLocations(visitLocationsCusDtos)
                    .build();

            return new ResponseObject("200", "OK", userLikeDtos);
        } catch (DataAccessException e) {
            return new ResponseObject("500", "Lỗi trong quá trình lưu", null);
        }
    }

}
