package com.main.traveltour.restcontroller.staff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.staff.TransportationFiltersDto;
import com.main.traveltour.dto.staff.TransportationSchedulesDto;
import com.main.traveltour.dto.staff.TransportationSearchDto;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.staff.TransportationScheduleService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/transportation-schedule-service/")
public class TransportationServiceAPI {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportationSchedulesRepository transportationSchedulesRepository;

    @GetMapping("find-all-transportation-schedule")
    public ResponseObject searchTransportationSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String transportationSearch,
            @RequestParam(required = false) String filters) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TransportationSearchDto searchDto = null;
            TransportationFiltersDto filtersDto = null;

            if (transportationSearch != null) {
                searchDto = objectMapper.readValue(transportationSearch, TransportationSearchDto.class);
            }

            if (filters != null) {
                filtersDto = objectMapper.readValue(filters, TransportationFiltersDto.class);
            }

            Timestamp departureTimestamp = searchDto != null && searchDto.getDepartureTime() != null ?
                    new Timestamp(searchDto.getDepartureTime().getTime()) : null;
            Timestamp arrivalTimestamp = searchDto != null && searchDto.getArrivalTime() != null ?
                    new Timestamp(searchDto.getArrivalTime().getTime()) : null;

            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Page<TransportationSchedules> transportationSchedules = null;
            if (searchDto != null || filtersDto != null) {
                transportationSchedules = transportationScheduleService.findTransportationSchedulesWithFilter(
                        searchDto.getFromLocation(),
                        searchDto.getToLocation(),
                        departureTimestamp,
                        arrivalTimestamp,
                        searchDto.getAmountSeat(),
                        filtersDto.getPrice(),
                        filtersDto.getMediaTypeList(),
                        filtersDto.getListOfVehicleManufacturers(),
                        PageRequest.of(page, size, sort));
            }

            if (Objects.requireNonNull(transportationSchedules).isEmpty()) {
                transportationSchedules = transportationScheduleService.findTransportationSchedulesWithFilter(
                        searchDto.getFromLocation(),
                        searchDto.getToLocation(),
                        departureTimestamp,
                        arrivalTimestamp,
                        searchDto.getAmountSeat(),
                        null,
                        null,
                        null,
                        PageRequest.of(page, size, sort));
                Page<TransportationSchedulesDto> transportationSchedulesDtos = transportationSchedules.map(
                        transportationSchedule -> EntityDtoUtils.convertToDto(transportationSchedule, TransportationSchedulesDto.class));
                return new ResponseObject("204", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);
            }

            Page<TransportationSchedulesDto> transportationSchedulesDtos = transportationSchedules.map(
                    transportationSchedule -> EntityDtoUtils.convertToDto(transportationSchedule, TransportationSchedulesDto.class));

            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        }
    }


    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<TransportationSchedules> transportationSchedules = Optional.ofNullable(transportationSchedulesRepository.findById(id));
        if (transportationSchedules.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            TransportationSchedulesDto transportationSchedulesDto = EntityDtoUtils.convertToDto(transportationSchedules.get(), TransportationSchedulesDto.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDto);
        }
    }

}
