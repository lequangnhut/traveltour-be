package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.admin.BedTypesServiceAD;
import com.main.traveltour.service.admin.RoomUilityServiceAD;
import com.main.traveltour.service.staff.RoomTypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    private RoomUilityServiceAD roomUilityServiceAD;

    @GetMapping("find-room-type-by-hotelId")
    public ResponseObject findRoomTypeByHotelId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String hotelId,
            @RequestParam(required = false) String searchTerm) {
        System.out.println(hotelId);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<RoomTypes> roomTypesPage = searchTerm != null && !searchTerm.isEmpty()
                ? roomTypeService.findByHotelIdWithUtilitiesAndSearchTerm(searchTerm, hotelId, PageRequest.of(page, size, sort))
                : roomTypeService.findByHotelIdAndIsDeletedIsFalse(hotelId, PageRequest.of(page, size, sort));

        if (roomTypesPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", roomTypesPage);
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

//    @GetMapping("find-room-utilities-name-by-roomTypeId/{id}")
//    public ResponseObject findRoomUtilitiesNameByRoomTypeId(@PathVariable String id) {
//        List<String> roomUtilitiesName = roomUilityServiceAD.findRoomUtilitiesNameByRoomTypeId(id);
//        if (roomUtilitiesName.isEmpty()) {
//            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
//        } else {
//            return new ResponseObject("200", "Đã tìm thấy dữ liệu", roomUtilitiesName);
//        }
//    }

}
