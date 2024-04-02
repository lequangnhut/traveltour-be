package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.HotelsDto;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/hotel-service/")
public class HotelServiceAPI {

    @Autowired
    private HotelServiceService hotelServiceService;

    @GetMapping("find-all-hotel")
    public ResponseObject searchHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date arrivalDate,
            @RequestParam(required = false) Integer numAdults,
            @RequestParam(required = false) Integer numChildren,
            @RequestParam(required = false) Integer numRooms) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<HotelsDto> hotelsDtos = hotelServiceService.findAvailableHotelsWithFilters
                (searchTerm, location, departureDate, arrivalDate, numAdults,
                        numChildren, numRooms, PageRequest.of(page, size, sort));

        return hotelsToResponseObject(hotelsDtos);
    }

    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<Hotels> hotels = Optional.ofNullable(hotelServiceService.getHotelsById(id));
        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    private ResponseObject hotelsToResponseObject(Page<HotelsDto> hotelsDtos) {
        if (hotelsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelsDtos);
        }
    }
}
