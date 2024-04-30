package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.RoomTypeAvailabilityDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.admin.BedTypesServiceAD;
import com.main.traveltour.service.staff.RoomTypeServiceService;
import com.main.traveltour.utils.ChangeCheckInTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/room-type/")
public class RoomTypeServiceAPI {

    @Autowired
    private RoomTypeServiceService roomTypeService;

    @Autowired
    private BedTypesServiceAD bedTypesServiceAD;

    @Autowired
    private ChangeCheckInTimeService changeCheckInTimeService;

    @GetMapping("find-room-type-by-hotelId")
    public ResponseObject findRoomTypeByHotelId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOut,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<RoomTypeAvailabilityDto> availableRoomTypes = searchTerm != null && !searchTerm.isEmpty()
                ? roomTypeService.findByHotelIdWithUtilitiesAndSearchTerm(searchTerm, hotelId, changeCheckInTimeService.changeCheckInTime(new Timestamp(checkIn.getTime())), changeCheckInTimeService.changeCheckInTime(new Timestamp(checkOut.getTime())), PageRequest.of(page, size, sort))
                : roomTypeService.findRoomAvailabilityByHotelIdAndDateRange(hotelId, changeCheckInTimeService.changeCheckInTime(new Timestamp(checkIn.getTime())), changeCheckInTimeService.changeCheckInTime(new Timestamp(checkOut.getTime())), PageRequest.of(page, size, sort));

        if (availableRoomTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", availableRoomTypes);
        }
    }


    @GetMapping("find-bed-type-name-by-roomTypeId/{id}")
    public ResponseObject findBedTypeNameByRoomTypeId(@PathVariable String id) {
        List<String> bedTypeName = bedTypesServiceAD.findByRoomTypeId(id);
        if (bedTypeName.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bedTypeName);
        }
    }

}
