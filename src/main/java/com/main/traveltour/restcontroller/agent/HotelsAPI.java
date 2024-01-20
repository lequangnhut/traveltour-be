package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.HotelsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelsAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private BedTypeService bedTypeService;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private HotelsTypeService hotelsTypeService;

    @Autowired
    private RoomUtilitiesService roomUtilitiesService;

    @Autowired
    private PlaceUtilitiesService placeUtilitiesService;

    @GetMapping("/agent/hotels/find-by-agency-id/{userId}")
    private Hotels findByUserId(@PathVariable int userId) {
        return hotelsService.findByAgencyId(userId);
    }

    @GetMapping("/agent/hotels/list-hotels")
    private ResponseObject findAllHotels() {
        List<Hotels> hotels = hotelsService.findAllListHotel();

        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    @GetMapping("/agent/hotels/list-hotels-type")
    private ResponseObject findAllHotelsTYpe() {
        List<HotelTypes> hotelType = hotelsTypeService.findAllHotelType();

        if (hotelType.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelType);
        }
    }

    @GetMapping("agent/hotels/list-place-utilities")
    private ResponseObject findAllPlaceUtilities() {
        List<PlaceUtilities> listPlaceUtilities = placeUtilitiesService.findAll();

        if (listPlaceUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", listPlaceUtilities);
        }
    }

    @GetMapping("agent/hotels/list-bed-type")
    private ResponseObject findAllListBedType() {
        List<BedTypes> bedTypes = bedTypeService.findAllListBedTypes();

        if (bedTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bedTypes);
        }
    }

    @GetMapping("agent/hotels/list-room-utilities")
    private ResponseObject findAllRoomUtilities() {
        List<RoomUtilities> roomUtilities = roomUtilitiesService.findAllRoomUtils();

        if (roomUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", roomUtilities);
        }
    }

    @PostMapping("agent/hotels/register-hotels")
    private void registerTransport(HotelsDto hotelsDto) throws IOException {

    }
}
