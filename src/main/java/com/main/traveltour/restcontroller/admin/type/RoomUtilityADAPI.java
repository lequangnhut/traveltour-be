package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.RoomUtilityDtoAD;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.RoomTypesServiceAD;
import com.main.traveltour.service.admin.RoomUilityServiceAD;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomUtilityADAPI {

    @Autowired
    private RoomUilityServiceAD roomUilityServiceAD;

    @Autowired
    private RoomTypesServiceAD roomTypesServiceAD;

    @GetMapping("admin/type/find-all-room-utility-type")
    private ResponseObject findAllBedType(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<RoomUtilities> items = searchTerm == null || searchTerm.isEmpty()
                ? roomUilityServiceAD.findAll(PageRequest.of(page, size, sort))
                : roomUilityServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-room-utility-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        RoomUtilities roomUtilities = roomUilityServiceAD.findById(id);
        if (roomUtilities != null) {
            RoomUtilityDtoAD dto = EntityDtoUtils.convertToDto(roomUtilities, RoomUtilityDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-room-utility-type-name/{name}")
    private ResponseObject checkDuplicateVisitTypeName(@PathVariable String name) {
        boolean exists = roomUilityServiceAD.findByRoomUtilityName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-room-utility-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<RoomTypes> roomTypes = roomTypesServiceAD.findByRoomUtilityTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !roomTypes.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-room-utility-type")
    private void createBedTypes(@RequestBody RoomUtilityDtoAD roomUtilityDtoAD) {
        RoomUtilities roomUtilities = EntityDtoUtils.convertToEntity(roomUtilityDtoAD, RoomUtilities.class);
        roomUtilities.setRoomUtilitiesName(roomUtilityDtoAD.getRoomUtilitiesName());
        roomUilityServiceAD.save(roomUtilities);
    }

    @PutMapping("admin/type/update-room-utility-type")
    private void updateBedTypes(@RequestBody RoomUtilityDtoAD roomUtilityDtoAD) {
        RoomUtilities roomUtilities = roomUilityServiceAD.findById(roomUtilityDtoAD.getId());
        roomUtilities.setRoomUtilitiesName(roomUtilityDtoAD.getRoomUtilitiesName());
        roomUilityServiceAD.save(roomUtilities);
    }

    @DeleteMapping("admin/type/delete-room-utility-type/{id}")
    private void deleteAccount(@PathVariable int id) {
        roomUilityServiceAD.delete(id);
    }
}
