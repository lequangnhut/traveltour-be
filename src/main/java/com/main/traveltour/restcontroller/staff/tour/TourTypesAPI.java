package com.main.traveltour.restcontroller.staff.tour;

import com.main.traveltour.dto.staff.tour.TourTypesDto;
import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.service.staff.TourTypesService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TourTypesAPI {

    private static final Logger logger = LoggerFactory.getLogger(TourTypesAPI.class);

    @Autowired
    private TourTypesService tourTypesService;

    @GetMapping("staff/tourType/find-by-id/{id}")
    public ResponseEntity<TourTypesDto> findById(@PathVariable int id) {
        try {
            TourTypes tourTypes = tourTypesService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TourType not exist with id: " + id));

            TourTypesDto tourTypesDto = EntityDtoUtils.convertToDto(tourTypes, TourTypesDto.class);
            return ResponseEntity.ok(tourTypesDto);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("staff/tourType/find-all")
    public ResponseEntity<List<TourTypes>> getAllTourTypes() {
        List<TourTypes> tourTypesList = tourTypesService.findAll();

        if (tourTypesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tourTypesList);
    }
}
