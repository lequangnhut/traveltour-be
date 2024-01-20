package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.PlaceUtilities;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.PlaceUtilitiesService;
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
    private PlaceUtilitiesService placeUtilitiesService;

    @Autowired
    private HotelsService hotelsService;

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

    @GetMapping("agent/hotels/list-place-utilities")
    private ResponseObject findAllPlaceUtilities() {
        List<PlaceUtilities> listPlaceUtilities = placeUtilitiesService.findAll();

        if (listPlaceUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", listPlaceUtilities);
        }
    }

    @PostMapping("agent/hotels/register-hotels")
    private void registerTransport() throws IOException {

    }
}
