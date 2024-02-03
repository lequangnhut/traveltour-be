package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.AgenciesDtoAD;
import com.main.traveltour.dto.agent.AgenciesDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.AgencyServiceAD;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgenciesADAPI {

    private static final Logger logger = LoggerFactory.getLogger(AgenciesADAPI.class);

    @Autowired
    private AgencyServiceAD agencyServiceAD;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("admin/agency/find-all-agency-waiting")
    private ResponseObject findAllAgencyAcceptOne(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) {

        int isAccepted = 1;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllWaiting(PageRequest.of(page, size, sort))
                : agencyServiceAD.findOneTrueWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }
    @GetMapping("admin/agency/find-all-agency-accepted")
    private ResponseObject findAllAgencyAcceptTwo(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) {

        int isAccepted = 2;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllAccepted(PageRequest.of(page, size, sort))
                : agencyServiceAD.findTwoTrueWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/find-all-agency-accepted-false")
    private ResponseObject findAllAgencyAcceptTwoButFalse(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) {

        int isAccepted = 2;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllAcceptedButFalse(PageRequest.of(page, size, sort))
                : agencyServiceAD.findTwoFalseWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/find-all-agency-denied")
    private ResponseObject findAllAgencyAcceptThree(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String searchTerm) {

        int isAccepted = 3;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllDenied(PageRequest.of(page, size, sort))
                : agencyServiceAD.findThreeTrueWithSearch(searchTerm, PageRequest.of(page, size, sort));
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }
    @GetMapping("admin/agency/count-all-agency-waiting")
    private ResponseObject countAllAgencyAcceptOne() {
        // Sử dụng phương thức tìm kiếm mới trong service
        Long items =  agencyServiceAD.countAllWaiting();
        if (items == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/find-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        Agencies agencies = agencyServiceAD.findbyId(id);
        if (agencies != null) {
            AgenciesDtoAD dto = EntityDtoUtils.convertToDto(agencies, AgenciesDtoAD.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/agency/check-duplicate-phone/{phone}")
    private ResponseObject checkDuplicatePhone(@PathVariable String phone) {
        boolean exists = agencyServiceAD.checkPhone(phone) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "SDT trùng lặp", response);
        } else {
            return new ResponseObject("200", "SDT hợp lệ", response);
        }
    }

    @GetMapping("admin/agency/check-duplicate-tax/{tax}")
    private ResponseObject checkDuplicateTax(@PathVariable String tax) {
        boolean exists = agencyServiceAD.checkTax(tax) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        if (exists) {
            return new ResponseObject("404", "MST trùng lặp", response);
        } else {
            return new ResponseObject("200", "MST hợp lệ", response);
        }
    }

    @PutMapping("admin/agency/update-agency")
    private void updateBedTypes(@RequestBody AgenciesDtoAD agenciesDtoAD) {
        Agencies agencies = agencyServiceAD.findbyId(agenciesDtoAD.getId());
        agencies.setImgDocument(agenciesDtoAD.getImgDocument());
        agencies.setNameAgency(agenciesDtoAD.getNameAgency());
        agencies.setRepresentativeName(agenciesDtoAD.getRepresentativeName());
        agencies.setTaxId(agenciesDtoAD.getTaxId());
        agencies.setUrlWebsite(agenciesDtoAD.getUrlWebsite());
        agencies.setPhone(agenciesDtoAD.getPhone());
        agencies.setProvince(agenciesDtoAD.getProvince());
        agencies.setDistrict(agenciesDtoAD.getDistrict());
        agencies.setWard(agenciesDtoAD.getWard());
        agencies.setAddress(agenciesDtoAD.getAddress());
        agencies.setIsActive(agenciesDtoAD.getIsActive());

        agencyServiceAD.save(agencies);
    }

    @PutMapping("admin/agency/update-agency/{id}")
    public ResponseEntity<Agencies> updateAgencyById(@PathVariable int id, @RequestPart("toursDto") AgenciesDto agenciesDto, @RequestPart("tourImg") MultipartFile tourImg) {
        try {
            agenciesDto.setId(id);
            agenciesDto.setIsAccepted(2);
            String imagesPath = fileUpload.uploadFile(tourImg);
            Agencies agencies = EntityDtoUtils.convertToEntity(agenciesDto, Agencies.class);
            agencies.setImgDocument(imagesPath);
            Agencies updatedAgency = agencyServiceAD.save(agencies);
            return ResponseEntity.ok(updatedAgency);
        } catch (Exception e) {
            logger.error("Error when updating tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("admin/agency/delete-agency/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAgencyById(@PathVariable int id) {
        try {
            Agencies agencies = agencyServiceAD.findbyId(id);
            agencies.setIsActive(Boolean.FALSE);
            agencyServiceAD.save(agencies);
            Map<String, Boolean> response = new HashMap<>();
            response.put("delete", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("admin/agency/restore-agency/{id}")
    public ResponseEntity<Map<String, Boolean>> restoreAgencyById(@PathVariable int id) {
        try {
            Agencies agencies = agencyServiceAD.findbyId(id);
            agencies.setIsActive(Boolean.TRUE);
            agencyServiceAD.save(agencies);
            Map<String, Boolean> response = new HashMap<>();
            response.put("restore", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("admin/agency/accepted-agency/{id}")
    public ResponseEntity<Map<String, Boolean>> acceptedAgencyById(@PathVariable int id) {
        try {
            Agencies agencies = agencyServiceAD.findbyId(id);
            agencies.setIsAccepted(2);
            agencyServiceAD.save(agencies);

            // Gửi email sau khi phục hồi
            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailAcceptedAgency(agenciesDto);

            Map<String, Boolean> response = new HashMap<>();
            response.put("restore", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("admin/agency/denied-agency/{id}")
    public ResponseEntity<Map<String, Boolean>> deniedAgencyById(@PathVariable int id) {
        try {
            Agencies agencies = agencyServiceAD.findbyId(id);
            agencies.setIsAccepted(3);
            agencyServiceAD.save(agencies);

            // Gửi email sau khi phục hồi
            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailDeniedAgency(agenciesDto);

            Map<String, Boolean> response = new HashMap<>();
            response.put("restore", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error when deactivate tour", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
