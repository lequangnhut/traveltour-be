package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.HotelUtilityDtoAD;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.HotelsServiceAD;
import com.main.traveltour.service.admin.PlaceUtilitiesServiceAD;
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
public class HotelUtilityADAPI {

    @Autowired
    private PlaceUtilitiesServiceAD placeUtilitiesServiceAD;

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @GetMapping("admin/type/find-all-hotel-utility-type")
    private ResponseObject findAllBedType(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<PlaceUtilities> items = searchTerm == null || searchTerm.isEmpty()
                ? placeUtilitiesServiceAD.findAll(PageRequest.of(page, size, sort))
                : placeUtilitiesServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-hotel-utility-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        PlaceUtilities placeUtilities = placeUtilitiesServiceAD.findById(id);
        if (placeUtilities != null) {
            HotelUtilityDtoAD dto = EntityDtoUtils.convertToDto(placeUtilities, HotelUtilityDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-hotel-utility-type-name/{name}")
    private ResponseObject checkDuplicateVisitTypeName(@PathVariable String name) {
        boolean exists = placeUtilitiesServiceAD.findByPlaceUtilityName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-hotel-utility-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<Hotels> hotels = hotelsServiceAD.findByUtility(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !hotels.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-hotel-utility-type")
    private void createBedTypes(@RequestBody HotelUtilityDtoAD hotelUtilityDtoAD) {
        PlaceUtilities placeUtilities = EntityDtoUtils.convertToEntity(hotelUtilityDtoAD, PlaceUtilities.class);
        placeUtilities.setPlaceUtilitiesName(hotelUtilityDtoAD.getPlaceUtilitiesName());
        placeUtilitiesServiceAD.save(placeUtilities);
    }

    @PutMapping("admin/type/update-hotel-utility-type")
    private void updateBedTypes(@RequestBody HotelUtilityDtoAD hotelUtilityDtoAD) {
        PlaceUtilities placeUtilities = placeUtilitiesServiceAD.findById(hotelUtilityDtoAD.getId());
        placeUtilities.setPlaceUtilitiesName(hotelUtilityDtoAD.getPlaceUtilitiesName());
        placeUtilitiesServiceAD.save(placeUtilities);
    }

    @DeleteMapping("admin/type/delete-hotel-utility-type/{id}")
    private void deleteAccount(@PathVariable int id) {
        placeUtilitiesServiceAD.delete(id);
    }
}
