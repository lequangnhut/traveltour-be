package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.VisitLocationTypesDtoAD;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.admin.VisitLocationTypesServiceAD;
import com.main.traveltour.service.admin.VisitLocationsServiceAD;
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
public class VisitLocationTypesADAPI {

    @Autowired
    private VisitLocationTypesServiceAD visitLocationTypesServiceAD;

    @Autowired
    private VisitLocationsServiceAD visitLocationsServiceAD;

    @GetMapping("admin/type/find-all-visit-location-type")
    private ResponseObject findAllVisitLocationType(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortDir,
                                                    @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<VisitLocationTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? visitLocationTypesServiceAD.findAll(PageRequest.of(page, size, sort))
                : visitLocationTypesServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-visit-location-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        VisitLocationTypes visitLocationTypes = visitLocationTypesServiceAD.findById(id);
        if (visitLocationTypes != null) {
            VisitLocationTypesDtoAD dto = EntityDtoUtils.convertToDto(visitLocationTypes, VisitLocationTypesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-visit-location-type-name/{name}")
    private ResponseObject checkDuplicateVisitTypeName(@PathVariable String name) {
        boolean exists = visitLocationTypesServiceAD.findByVisitLocationTypeName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-visit-location-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<VisitLocations> visitLocations = visitLocationsServiceAD.findByVisitLocationTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !visitLocations.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-visit-location-type")
    private void createVisitLocationTypes(@RequestBody VisitLocationTypesDtoAD visitLocationTypesDtoAD) {
        VisitLocationTypes visitLocationTypes = EntityDtoUtils.convertToEntity(visitLocationTypesDtoAD, VisitLocationTypes.class);
        visitLocationTypes.setVisitLocationTypeName(visitLocationTypesDtoAD.getVisitLocationTypeName());
        visitLocationTypesServiceAD.save(visitLocationTypes);
    }

    @PutMapping("admin/type/update-visit-location-type")
    private void updateVisitLocationTypes(@RequestBody VisitLocationTypesDtoAD visitLocationTypesDtoAD) {
        VisitLocationTypes visitLocationTypes = visitLocationTypesServiceAD.findById(visitLocationTypesDtoAD.getId());
        visitLocationTypes.setVisitLocationTypeName(visitLocationTypesDtoAD.getVisitLocationTypeName());
        visitLocationTypesServiceAD.save(visitLocationTypes);
    }

    @DeleteMapping("admin/type/delete-visit-location-type/{id}")
    private void deleteAccount(@PathVariable int id) {
        visitLocationTypesServiceAD.delete(id);
    }
}
