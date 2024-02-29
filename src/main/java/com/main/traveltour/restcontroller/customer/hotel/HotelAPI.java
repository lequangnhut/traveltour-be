package com.main.traveltour.restcontroller.customer.hotel;

import com.main.traveltour.dto.agent.hotel.HotelCustomerDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeDto;
import com.main.traveltour.entity.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.main.traveltour.service.agent.*;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelAPI {
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
                new Timestamp(dateFomatCheckin.getTime()), new Timestamp(dateFomatCheckout.getTime()), page, size,
                sort);

        List<RoomTypeDto> roomTypeDto = EntityDtoUtils.convertToDtoList(hotel.getRoomTypes(), RoomTypeDto.class);
        return new ResponseObjectAndPages("200", "OK", roomTypeDto, hotel.getTotalCount());
    }
}
