package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.staff.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/hotel-service/")
public class HotelServiceAPI {

    private final HotelServiceService hotelServiceService;

    @Autowired
    public HotelServiceAPI(HotelServiceService hotelServiceService) {
        this.hotelServiceService = hotelServiceService;
    }

    @GetMapping("find-all-hotel")
    public ResponseObject searchHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date arrivalDate,
            @RequestParam(required = false) Integer numAdults,
            @RequestParam(required = false) Integer numChildren,
            @RequestParam(required = false) Integer numRooms) {

        Timestamp departureTimestamp = departureDate != null ? new Timestamp(departureDate.getTime()) : null;
        Timestamp arrivalTimestamp = arrivalDate != null ? new Timestamp(arrivalDate.getTime()) : null;

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Hotels> hotels = searchTerm != null && !searchTerm.isEmpty()
                ? hotelServiceService.findBySearchTerm(searchTerm, PageRequest.of(page, size, sort))
                : location != null || departureDate != null || arrivalDate != null || numAdults != null || numChildren != null
                ? hotelServiceService.findAvailableHotelsWithFilters(location, departureTimestamp, arrivalTimestamp, numAdults, numChildren, numRooms, PageRequest.of(page, size, sort))
                : hotelServiceService.findAllHotel(PageRequest.of(page, size, sort));

        return hotelsToResponseObject(hotels);
    }


    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        System.out.println();
        Optional<Hotels> hotels = Optional.ofNullable(hotelServiceService.getHotelsById(id));
        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    private ResponseObject hotelsToResponseObject(Page<Hotels> hotels) {
        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }
}
