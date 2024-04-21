package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.AgenciesDtoAD;
import com.main.traveltour.dto.admin.AgenciesGetDataDto;
import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.admin.AgencyServiceAD;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgenciesADAPI {

    @Autowired
    private AgencyServiceAD agencyServiceAD;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("admin/agency/find-all-agency-accepted")
    private ResponseObject findAllAgencyAcceptTwo(@RequestParam Boolean isActive,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir,
                                                  @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllAgenciesByAccepted(PageRequest.of(page, size, sort), isActive)
                : agencyServiceAD.findAllAgenciesByAcceptedWithSearch(searchTerm, PageRequest.of(page, size, sort), isActive);

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/find-all-agency-waiting")
    private ResponseObject findAllAgencyAcceptOne(@RequestParam Integer isAccepted,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortDir,
                                                  @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Agencies> items = searchTerm == null || searchTerm.isEmpty()
                ? agencyServiceAD.findAllAgenciesWaitingByIsAccepted(PageRequest.of(page, size, sort), isAccepted)
                : agencyServiceAD.findAllAgenciesWaitingByIsAcceptedWithSearch(searchTerm, PageRequest.of(page, size, sort), isAccepted);

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/count-all-agency-waiting")
    private ResponseObject countAllAgencyAcceptOne() {
        Long items = agencyServiceAD.countAllWaiting();

        if (items == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("admin/agency/find-by-id/{agenciesId}")
    private ResponseObject findByAgenciesId(@PathVariable Integer agenciesId) {
        Agencies agencies = agencyServiceAD.findById(agenciesId);
        AgenciesGetDataDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesGetDataDto.class);

        if (agenciesDto != null) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", agenciesDto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin/agency/find-by-id-note/{id}")
    private ResponseObject findByIdNote(@PathVariable int id) {
        List<Agencies> agencies = agencyServiceAD.findByIdAgencyId(id);

        if (agencies != null) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", agencies);
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
    private void updateAgencies(@RequestBody AgenciesDtoAD agenciesDtoAD) {
        Agencies agencies = agencyServiceAD.findById(agenciesDtoAD.getId());
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

    @PutMapping("admin/agency/update-agency/{agenciesId}")
    public ResponseObject updateAgencyById(@PathVariable Integer agenciesId,
                                           @RequestPart AgenciesDto agenciesDto,
                                           @RequestPart(required = false) MultipartFile agenciesImg) {
        try {
            Agencies agenciesDB = agencyServiceAD.findById(agenciesId);

            if (agenciesImg != null) {
                String imagesPath = fileUpload.uploadFile(agenciesImg);

                Agencies agencies = EntityDtoUtils.convertToEntity(agenciesDto, Agencies.class);
                agencies.setId(agenciesId);
                agencies.setIsAccepted(2);
                agencies.setImgDocument(imagesPath);
                agencyServiceAD.save(agencies);
            } else {
                Agencies agencies = EntityDtoUtils.convertToEntity(agenciesDto, Agencies.class);
                agencies.setId(agenciesId);
                agencies.setIsAccepted(2);
                agencies.setImgDocument(agenciesDB.getImgDocument());
                agencyServiceAD.save(agencies);
            }

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @DeleteMapping("admin/agency/delete-agency/{agenciesId}")
    public ResponseObject deleteAgencyById(@PathVariable Integer agenciesId,
                                           @RequestParam String noted) {
        try {
            Agencies agencies = agencyServiceAD.findById(agenciesId);
            agencies.setIsActive(Boolean.FALSE);
            agencies.setNoted(noted);
            agencies.setDateBlocked(new Timestamp(System.currentTimeMillis()));
            agencyServiceAD.save(agencies);

            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailDeleteAgency(agenciesDto);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PutMapping("admin/agency/restore-agency/{agenciesId}")
    public ResponseObject restoreAgencyById(@PathVariable Integer agenciesId) {
        try {
            Agencies agencies = agencyServiceAD.findById(agenciesId);
            agencies.setIsActive(Boolean.TRUE);
            agencies.setNoted(null);
            agencies.setDateBlocked(null);
            agencyServiceAD.save(agencies);

            // Gửi email sau khi xác nhận doanh nghiệp
            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailAcceptedAgency(agenciesDto);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PutMapping("admin/agency/accepted-agency/{agenciesId}")
    public ResponseObject acceptedAgencyById(@PathVariable Integer agenciesId) {
        try {
            Agencies agencies = agencyServiceAD.findById(agenciesId);
            agencies.setIsAccepted(2); // duyệt hồ sơ
            agencies.setNoted(null);
            agencies.setDateAccepted(new Timestamp(System.currentTimeMillis()));
            agencyServiceAD.save(agencies);

            // Gửi email sau khi xác nhận doanh nghiệp
            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailAcceptedAgency(agenciesDto);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @PutMapping("admin/agency/denied-agency/{agenciesId}")
    public ResponseObject deniedAgencyById(@PathVariable Integer agenciesId,
                                           @RequestParam String noted) {
        try {
            Agencies agencies = agencyServiceAD.findById(agenciesId);
            agencies.setIsAccepted(3); // từ chối hồ sơ
            agencies.setNoted(noted);
            agencies.setDateBlocked(new Timestamp(System.currentTimeMillis()));
            agencyServiceAD.save(agencies);

            // Gửi email sau khi từ chối doanh nghiệp
            AgenciesDto agenciesDto = EntityDtoUtils.convertToDto(agencies, AgenciesDto.class);
            emailService.queueEmailDeniedAgency(agenciesDto);

            return new ResponseObject("200", "Thành công", null);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }
}
