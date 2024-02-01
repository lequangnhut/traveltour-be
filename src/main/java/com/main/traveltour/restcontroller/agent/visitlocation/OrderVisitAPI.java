package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.dto.agent.visit_location.OrderVisitsDto;
import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.agent.OrderVisitService;
import com.main.traveltour.service.agent.VisitLocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderVisitAPI {

    @Autowired
    private OrderVisitService orderVisitService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @GetMapping("/agent/order-visit/find-all-order-visit/{brandId}")
    private ResponseEntity<Page<OrderVisits>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String sortDir,
                                                                    @RequestParam(required = false) String searchTerm,
                                                                    @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<OrderVisits> orderTransportations = searchTerm == null || searchTerm.isEmpty()
                ? orderVisitService.findAllOrderVisits(brandId, PageRequest.of(page, size, sort))
                : orderVisitService.findAllOrderVisitsWithSearch(brandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(orderTransportations, HttpStatus.OK);
    }

    @GetMapping("/agent/order-visit/find-all-visit-location/{visitLocationId}")
    private ResponseObject findByVisitLocationId(@PathVariable String visitLocationId) {
        List<VisitLocations> visitLocations = visitLocationsService.findAllByVisitLocationId(visitLocationId);

        if (visitLocations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }
}
