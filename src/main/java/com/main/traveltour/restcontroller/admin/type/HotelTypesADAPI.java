package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.HotelTypesDtoAD;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.HotelTypesServiceAD;
import com.main.traveltour.service.admin.HotelsServiceAD;
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
public class HotelTypesADAPI {

    @Autowired
    private HotelTypesServiceAD hotelTypesServiceAD;

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @GetMapping("admin/type/find-all-hotel-type")
    private ResponseObject findAllHotelType(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<HotelTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? hotelTypesServiceAD.findAll(PageRequest.of(page, size, sort))
                : hotelTypesServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-hotel-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        HotelTypes hotelTypes = hotelTypesServiceAD.findById(id);
        if (hotelTypes != null) {
            HotelTypesDtoAD dto = EntityDtoUtils.convertToDto(hotelTypes, HotelTypesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-hotel-type-name/{name}")
    private ResponseObject checkDuplicateHotelTypeName(@PathVariable String name) {
        boolean exists = hotelTypesServiceAD.findByHotelTypeName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-hotel-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<Hotels> hotels = hotelsServiceAD.findByHotelTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !hotels.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-hotel-type")
    private void createHotelTypes(@RequestBody HotelTypesDtoAD hotelTypesDtoAD) {
        HotelTypes hotelTypes = EntityDtoUtils.convertToEntity(hotelTypesDtoAD, HotelTypes.class);
        hotelTypes.setHotelTypeName(hotelTypesDtoAD.getHotelTypeName());
        hotelTypesServiceAD.save(hotelTypes);
    }

    @PutMapping("admin/type/update-hotel-type")
    private void updateHotelTypes(@RequestBody HotelTypesDtoAD hotelTypesDtoAD) {
        HotelTypes hotelTypes = hotelTypesServiceAD.findById(hotelTypesDtoAD.getId());
        hotelTypes.setHotelTypeName(hotelTypesDtoAD.getHotelTypeName());
        hotelTypesServiceAD.save(hotelTypes);
    }

    @DeleteMapping("admin/type/delete-hotel-type/{id}")
    private void deleteHotel(@PathVariable int id) {
        hotelTypesServiceAD.delete(id);
    }
}
