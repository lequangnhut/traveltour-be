package com.main.traveltour.restcontroller.customer.hotel;

import com.main.traveltour.dto.agent.hotel.HotelCustomerDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeDto;
import com.main.traveltour.entity.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.main.traveltour.repository.UserCommentsRepository;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.customer.UserCommentsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelCustomerAPI {
    @Autowired
    private HotelsService hotelsService;
    @Autowired
    private HotelsTypeService hotelsTypeService;
    @Autowired
    private PlaceUtilitiesService placeUtilitiesService;
    @Autowired
    private BedTypeService bedTypeService;
    @Autowired
    private RoomUtilitiesService roomUtilitiesService;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private UserCommentsService userCommentsService;

    /**
     * Phương thức tìm kiếm tất cả khách sạn
     *
     * @return
     */
    @GetMapping("customer/hotels/findAllHotel")
    public ResponseObject findAllHotel() {
        List<Hotels> hotels = hotelsService.getAllHotels();
        List<HotelCustomerDto> hotelCustomerDtos = EntityDtoUtils.convertToDtoList(hotels, HotelCustomerDto.class);
        return new ResponseObject("200", "OK", hotelCustomerDtos);
    }

    @GetMapping("customer/hotels/findAllHotelType")
    public ResponseObject findAllHotelType() {
        List<HotelTypes> hotelTypes = hotelsTypeService.findAllHotelType();
        return new ResponseObject("200", "OK", hotelTypes);
    }

    @GetMapping("customer/hotels/findAllPlaceUtilities")
    public ResponseObject findAllPlaceUtilities() {
        List<PlaceUtilities> placeUtilities = placeUtilitiesService.findAll();
        return new ResponseObject("200", "OK", placeUtilities);
    }

    @GetMapping("customer/hotels/findAllRoomBedType")
    public ResponseObject findAllRoomBeds() {
        List<BedTypes> roomBeds = bedTypeService.findAllListBedTypes();
        return new ResponseObject("200", "OK", roomBeds);
    }

    @GetMapping("customer/hotels/findAllRoomUtilities")
    public ResponseObject findAllRoomUtilities() {
        List<RoomUtilities> roomUtilities = roomUtilitiesService.findAllRoomUtils();
        return new ResponseObject("200", "OK", roomUtilities);
    }

    @GetMapping("/customer/hotels/findAllRoomTypesByFillter")
    public ResponseObjectAndPages findAllRoomTypesByFillter(
            @RequestParam(required = false) BigDecimal priceFilter,
            @RequestParam(required = false) List<Integer> hotelTypeIdListFilter,
            @RequestParam(required = false) List<Integer> placeUtilitiesIdListFilter,
            @RequestParam(required = false) List<Integer> roomUtilitiesIdListFilter,
            @RequestParam(required = false) Boolean breakfastIncludedFilter,
            @RequestParam(required = false) Boolean freeCancellationFilter,
            @RequestParam(required = false) List<Integer> roomBedsIdListFilter,
            @RequestParam(required = false) Integer amountRoomFilter,
            @RequestParam(required = false) String locationFilter,
            @RequestParam(required = false) Integer capacityAdultsFilter,
            @RequestParam(required = false) Integer capacityChildrenFilter,
            @RequestParam(required = false) String checkInDateFiller,
            @RequestParam(required = false) String checkOutDateFiller,
            @RequestParam(required = false) String hotelIdFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String sort) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date dateFomatCheckin = dateFormat.parse(checkInDateFiller);
        Date dateFomatCheckout = dateFormat.parse(checkOutDateFiller);

        RoomTypeCustomerDto hotel = roomTypeService.findRoomTypesWithFiltersCustomer(
                priceFilter, hotelTypeIdListFilter, placeUtilitiesIdListFilter,
                roomUtilitiesIdListFilter, breakfastIncludedFilter,
                freeCancellationFilter, roomBedsIdListFilter, amountRoomFilter,
                locationFilter, capacityAdultsFilter, capacityChildrenFilter,
                false, false,
                new Timestamp(dateFomatCheckin.getTime()), new Timestamp(dateFomatCheckout.getTime()),
                hotelIdFilter, page, size,
                sort);

        List<RoomTypeDto> roomTypeDto = EntityDtoUtils.convertToDtoList(hotel.getRoomTypes(), RoomTypeDto.class);
        roomTypeDto.forEach(roomType -> roomType.setRate(userCommentsService.findScoreRatingByRoomTypeId(roomType.getId())));
        roomTypeDto.forEach(roomType -> roomType.setCountRating(userCommentsService.findCountRatingByRoomTypeId(roomType.getId())));
        return new ResponseObjectAndPages("200", "OK", roomTypeDto, hotel.getTotalCount());
    }

    @GetMapping("customer/hotels/findAllRoomTypeByIds")
    public ResponseObject findAllRoomTypeByIds(
            @RequestParam(required = false) List<String> ids) {
        List<RoomTypes> roomTypes = roomTypeService.findAllRoomTypeByIds(ids);
        return new ResponseObject("200", "OK", roomTypes);
    }

    @GetMapping("customer/hotels/findAllRoomTypeByEncryptedData")
    public ResponseObject findAllRoomTypeByEncryptedData(
            @RequestParam(required = false) String encryptedData) {
        System.out.println(encryptedData);
//        List<RoomTypes> roomTypes = roomTypeService.findRoomTypesWithFiltersCustomer(encryptedData);
        return new ResponseObject("200", "OK", encryptedData);
    }
}
