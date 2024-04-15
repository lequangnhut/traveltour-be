package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.TourTypesDtoAD;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.TourTypesServiceAD;
import com.main.traveltour.service.admin.ToursServiceAD;
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
public class TourTypesADAPI {

    @Autowired
    private TourTypesServiceAD tourTypesServiceAD;

    @Autowired
    private ToursServiceAD toursServiceAD;

    @GetMapping("admin/type/find-all-tour-type")
    private ResponseObject findAllTourType(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir,
                                           @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TourTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? tourTypesServiceAD.findAll(PageRequest.of(page, size, sort))
                : tourTypesServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-tour-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        TourTypes tourTypes = tourTypesServiceAD.findById(id);
        if (tourTypes != null) {
            TourTypesDtoAD dto = EntityDtoUtils.convertToDto(tourTypes, TourTypesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-tour-type-name/{name}")
    private ResponseObject checkDuplicateTourTypeName(@PathVariable String name) {
        boolean exists = tourTypesServiceAD.findByTourTypeName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-tour-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<Tours> tours = toursServiceAD.findByTourTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !tours.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-tour-type")
    private void createTourTypes(@RequestBody TourTypesDtoAD tourTypesDtoAD) {
        TourTypes tourTypes = EntityDtoUtils.convertToEntity(tourTypesDtoAD, TourTypes.class);
        tourTypes.setTourTypeName(tourTypesDtoAD.getTourTypeName());
        tourTypesServiceAD.save(tourTypes);
    }

    @PutMapping("admin/type/update-tour-type")
    private void updateTourTypes(@RequestBody TourTypesDtoAD tourTypesDtoAD) {
        TourTypes tourTypes = tourTypesServiceAD.findById(tourTypesDtoAD.getId());
        tourTypes.setTourTypeName(tourTypesDtoAD.getTourTypeName());
        tourTypesServiceAD.save(tourTypes);
    }

    @DeleteMapping("admin/type/delete-tour-type/{id}")
    private void deleteTour(@PathVariable int id) {
        tourTypesServiceAD.delete(id);
    }
}
