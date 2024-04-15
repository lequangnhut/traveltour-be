package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.TransTypesDtoAD;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.entity.Transportations;
import com.main.traveltour.service.admin.TransServiceAD;
import com.main.traveltour.service.admin.TransTypeServiceAD;
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
public class TransTypesADAPI {

    @Autowired
    private TransTypeServiceAD transTypeServiceAD;

    @Autowired
    private TransServiceAD transServiceAD;

    @GetMapping("admin/type/find-all-trans-type")
    private ResponseObject findAllTransType(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TransportationTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? transTypeServiceAD.findAll(PageRequest.of(page, size, sort))
                : transTypeServiceAD.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/type/find-trans-type-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        TransportationTypes transportationTypes = transTypeServiceAD.findById(id);
        if (transportationTypes != null) {
            TransTypesDtoAD dto = EntityDtoUtils.convertToDto(transportationTypes, TransTypesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/type/check-duplicate-trans-type-name/{name}")
    private ResponseObject checkDuplicateTransTypeName(@PathVariable String name) {
        boolean exists = transTypeServiceAD.findByTransportationTypeName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("admin/type/check-trans-type-working/{id}")
    private ResponseObject checkTypeIsUsing(@PathVariable int id) {
        List<Transportations> transportations = transServiceAD.findbyTransTypeId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !transportations.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("admin/type/create-trans-type")
    private void createTransTypes(@RequestBody TransTypesDtoAD transTypesDtoAD) {
        TransportationTypes transportationTypes = EntityDtoUtils.convertToEntity(transTypesDtoAD, TransportationTypes.class);
        transportationTypes.setTransportationTypeName(transTypesDtoAD.getTransportationTypeName());
        transTypeServiceAD.save(transportationTypes);
    }

    @PutMapping("admin/type/update-trans-type")
    private void updateTransTypes(@RequestBody TransTypesDtoAD transTypesDtoAD) {
        TransportationTypes transportationTypes = transTypeServiceAD.findById(transTypesDtoAD.getId());
        transportationTypes.setTransportationTypeName(transTypesDtoAD.getTransportationTypeName());
        transTypeServiceAD.save(transportationTypes);
    }

    @DeleteMapping("admin/type/delete-trans-type/{id}")
    private void deleteTrans(@PathVariable int id) {
        transTypeServiceAD.delete(id);
    }
}
