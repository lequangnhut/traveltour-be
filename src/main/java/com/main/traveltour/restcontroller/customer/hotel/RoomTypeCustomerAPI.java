package com.main.traveltour.restcontroller.customer.hotel;



import com.google.gson.Gson;
import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeDto;
import com.main.traveltour.dto.customer.hotel.FillerDataDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.ResponseObjectAndPages;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.utils.Base64Utils;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomTypeCustomerAPI {
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
    private Gson gson;

    @Autowired
    private AgenciesService agenciesService;

    @GetMapping("customer/room-types/findAllRoomTypesByEncryptedData")
    public ResponseObjectAndPages findAllRoomTypesByEncryptedData(
            @RequestParam("encryptedData") String encryptedData
            ) throws ParseException {
        FillerDataDto fillerDataDto = Base64Utils.decodeToObject(encryptedData, FillerDataDto.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date dateFomatCheckin = dateFormat.parse(fillerDataDto.getCheckInDateFiller());
        Date dateFomatCheckout = dateFormat.parse(fillerDataDto.getCheckOutDateFiller());

        RoomTypeCustomerDto hotel = roomTypeService.findRoomTypesWithFiltersCustomer(
                fillerDataDto.getPriceFilter(), fillerDataDto.getHotelTypeIdListFilter(), fillerDataDto.getPlaceUtilitiesIdListFilter(),
                fillerDataDto.getRoomUtilitiesIdListFilter(), fillerDataDto.getBreakfastIncludedFilter(),
                fillerDataDto.getFreeCancellationFilter(), fillerDataDto.getRoomBedsIdListFilter(), fillerDataDto.getAmountRoomFilter(),
                fillerDataDto.getLocationFilter(), fillerDataDto.getCapacityAdultsFilter(), fillerDataDto.getCapacityChildrenFilter(),
                false, false,
                new Timestamp(dateFomatCheckin.getTime()), new Timestamp(dateFomatCheckout.getTime()),
                fillerDataDto.getHotelIdFilter(), fillerDataDto.getPage(), fillerDataDto.getSize(),
                fillerDataDto.getSort());

        List<RoomTypeDto> roomTypeDto = EntityDtoUtils.convertToDtoList(hotel.getRoomTypes(), RoomTypeDto.class);
        return new ResponseObjectAndPages("200", "OK", roomTypeDto, hotel.getTotalCount());
    }

    @GetMapping("customer/room-types/findRoomTypeByIdCustomer")
    public ResponseObject findRoomTypeByIdCustomer(@RequestParam(required = false) List<String> ids)  {
        List<RoomTypeDto> roomTypeDto = EntityDtoUtils.convertToDtoList(roomTypeService.findAllRoomTypeByIds(ids), RoomTypeDto.class);
        return new ResponseObject("200", "OK", roomTypeDto);
    }

    @GetMapping("customer/room-types/findUserByAgencyId")
    public ResponseObject findUserByAgencyId(@RequestParam("agencyId") Integer agencyId) {
        return new ResponseObject("200", "OK", agenciesService.findByAgencyId(agencyId));
    }
}
