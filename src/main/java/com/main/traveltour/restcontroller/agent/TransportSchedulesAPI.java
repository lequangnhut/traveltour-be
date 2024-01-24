package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.service.agent.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportSchedulesAPI {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @GetMapping("/agent/transportation-schedules/find-all-schedules/{transportBrandId}")
    private ResponseEntity<Page<TransportationSchedules>> findAllSchedule(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @RequestParam(defaultValue = "asc") String sortDir,
                                                                          @RequestParam(required = false) String searchTerm,
                                                                          @PathVariable String transportBrandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportationSchedules> transportationBrands = searchTerm == null || searchTerm.isEmpty()
                ? transportationScheduleService.findAllSchedules(transportBrandId, PageRequest.of(page, size, sort))
                : transportationScheduleService.findAllSchedulesWitchSearch(transportBrandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(transportationBrands, HttpStatus.OK);
    }
}
