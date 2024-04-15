package com.main.traveltour.restcontroller.admin.type;

import com.main.traveltour.dto.admin.type.TransportUtilitiesDto;
import com.main.traveltour.dto.admin.type.TransportUtilitiesGetDataDTO;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportUtilities;
import com.main.traveltour.service.admin.TransportUtilityServiceAD;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/admin/type/transport-utilities/")
public class TransportUtilityADAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private TransportUtilityServiceAD transportUtilityServiceAD;

    @GetMapping("find-all-transport-utilities")
    private ResponseObject findAllTransportUtility(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir,
                                                   @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportUtilities> transportUtilities = searchTerm == null || searchTerm.isEmpty()
                ? transportUtilityServiceAD.findAllTransUtilityAD(PageRequest.of(page, size, sort))
                : transportUtilityServiceAD.findAllWithSearchTransUtilityAD(searchTerm, PageRequest.of(page, size, sort));
        Page<TransportUtilitiesGetDataDTO> transportUtilitiesDto = transportUtilities.map(utilities -> EntityDtoUtils.convertToDto(utilities, TransportUtilitiesGetDataDTO.class));

        if (transportUtilitiesDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportUtilitiesDto);
        }
    }

    @GetMapping("find-transport-utilities-by-id/{transUtilityId}")
    private ResponseObject findTransUtilityById(@PathVariable int transUtilityId) {
        TransportUtilities transportUtilities = transportUtilityServiceAD.findTransUtilityADById(transUtilityId);

        if (transportUtilities == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportUtilities);
        }
    }

    @PostMapping(value = "create-transport-utilities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseObject createTransUtility(@RequestPart TransportUtilitiesDto transportUtilitiesDto,
                                              @RequestPart("icon") MultipartFile icon) {
        try {
            String iconUtility = fileUpload.uploadFile(icon);

            TransportUtilities transportUtilities = EntityDtoUtils.convertToEntity(transportUtilitiesDto, TransportUtilities.class);
            transportUtilities.setIcon(iconUtility);
            return new ResponseObject("200", "Thành công", transportUtilityServiceAD.save(transportUtilities));
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    @PutMapping(value = "update-transport-utilities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseObject updateTransUtility(@RequestPart TransportUtilitiesDto transportUtilitiesDto,
                                              @RequestPart(value = "icon", required = false) MultipartFile icon) {
        try {
            TransportUtilities transportUtilities = transportUtilityServiceAD.findTransUtilityADById(transportUtilitiesDto.getId());

            if (icon != null) {
                String iconUtility = fileUpload.uploadFile(icon);
                transportUtilities.setIcon(iconUtility);
                transportUtilities.setTitle(transportUtilitiesDto.getTitle());
                transportUtilities.setDescription(transportUtilitiesDto.getDescription());
                transportUtilityServiceAD.save(transportUtilities);
            } else {
                transportUtilities.setTitle(transportUtilitiesDto.getTitle());
                transportUtilities.setDescription(transportUtilitiesDto.getDescription());
                transportUtilityServiceAD.save(transportUtilities);
            }
            return new ResponseObject("200", "Thành công", "ok");
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    @DeleteMapping("delete-transport-utilities/{transUtilityId}")
    private void deleteAccount(@PathVariable int transUtilityId) {
        transportUtilityServiceAD.delete(transUtilityId);
    }

    @GetMapping("check-duplicate-trans-utility-name/{name}")
    private ResponseObject checkDuplicateTransUtilityName(@PathVariable String name) {
        boolean exists = transportUtilityServiceAD.findByDescription(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        if (exists) {
            return new ResponseObject("404", "Tên loại bị trùng lặp", response);
        } else {
            return new ResponseObject("200", "Tên hợp lệ", response);
        }
    }

    @GetMapping("check-trans-utility-working/{id}")
    private ResponseObject checkTransUtilityIsUsing(@PathVariable int id) {
        List<TransportUtilities> utility = transportUtilityServiceAD.findAllByUtilityId(id);
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = !utility.isEmpty();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", response);
        }
    }
}
