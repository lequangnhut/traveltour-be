package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.VisitLocationsDto;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.VisitLocationService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/visit-location-service/")
public class VisitLocationServiceAPI {

    @Autowired
    private VisitLocationService visitLocationService;

    @GetMapping("find-all-visit-location")
    public ResponseObject searchVisitLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String location) {


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<VisitLocations> visitLocations = searchTerm != null && !searchTerm.isEmpty()
                ? visitLocationService.findBySearchTerm(searchTerm, PageRequest.of(page, size, sort))
                : location != null
                ? visitLocationService.findVisitLocationsByProvince(location, PageRequest.of(page, size, sort))
                : visitLocationService.getAllByIsActiveIsTrueAndIsAcceptedIsTrue(PageRequest.of(page, size, sort));

        Page<VisitLocationsDto> visitLocationsDtos = visitLocations.map(visitLocation -> EntityDtoUtils.convertToDto(visitLocation, VisitLocationsDto.class));

        return visitLocationsToResponseObject(visitLocationsDtos);
    }


    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<VisitLocations> visitLocations = Optional.ofNullable(visitLocationService.findByIdAndIsActiveIsTrue(id));
        if (visitLocations.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    private ResponseObject visitLocationsToResponseObject(Page<VisitLocationsDto> visitLocations) {
        if (visitLocations.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }
}
