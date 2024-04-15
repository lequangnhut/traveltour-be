package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.BedTypesDtoAD;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.BedTypesServiceAD;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
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
public class BedTypesADAPI {

    @Autowired
    private BedTypesServiceAD bedTypesServiceAD;

    @Autowired
    private RoomBedsServiceAD roomBedsServiceAD;

    @GetMapping("admin/type/find-all-bed-type")
    private ResponseObject findAllBedType(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<BedTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? bedTypesServiceAD.findAll(PageRequest.of(page, size, sort))
                : bedTypesServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-bed-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        BedTypes bedTypes = bedTypesServiceAD.findById(id);

        if (bedTypes != null) {
            BedTypesDtoAD dto = EntityDtoUtils.convertToDto(bedTypes, BedTypesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-bed-type-name/{name}")
    private ResponseObject checkDuplicateVisitTypeName(@PathVariable String name) {
        boolean exists = bedTypesServiceAD.findByBedTypeName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-bed-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<RoomBeds> roomBeds = roomBedsServiceAD.findByBedTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !roomBeds.isEmpty();
        response.put("exists", exists);

        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-bed-type")
    private void createBedTypes(@RequestBody BedTypesDtoAD BedTypesDtoAD) {
        BedTypes bedTypes = EntityDtoUtils.convertToEntity(BedTypesDtoAD, BedTypes.class);
        bedTypes.setBedTypeName(BedTypesDtoAD.getBedTypeName());
        bedTypesServiceAD.save(bedTypes);
    }

    @PutMapping("admin/type/update-bed-type")
    private void updateBedTypes(@RequestBody BedTypesDtoAD BedTypesDtoAD) {
        BedTypes bedTypes = bedTypesServiceAD.findById(BedTypesDtoAD.getId());
        bedTypes.setBedTypeName(BedTypesDtoAD.getBedTypeName());
        bedTypesServiceAD.save(bedTypes);
    }

    @DeleteMapping("admin/type/delete-bed-type/{id}")
    private void deleteAccount(@PathVariable int id) {
        bedTypesServiceAD.delete(id);
    }
}
