package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.agent.transport.TransportationSchedulesDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationScheduleSeats;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.Transportations;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.agent.TransportationService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.ReplaceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class TransportSchedulesAPI {

    @Autowired
    private TransportationService transportationService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportScheduleSeatService transportScheduleSeatService;

    @GetMapping("/agent/transportation-schedules/find-all-schedule-by-trip-type/{tripType}")
    private ResponseObject findScheduleByTripType(@PathVariable Boolean tripType,
                                                  @RequestParam String transportBrandId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "desc") String sortDir,
                                                  @RequestParam(required = false) String searchTerm) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<TransportationSchedules> transportationSchedules = searchTerm == null || searchTerm.isEmpty()
                    ? transportationScheduleService.findAllScheduleAgent(transportBrandId, tripType, PageRequest.of(page, size, sort))
                    : transportationScheduleService.findAllScheduleAgentWitchSearch(transportBrandId, tripType, searchTerm, PageRequest.of(page, size, sort));
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedules);
        } catch (Exception e) {
            return new ResponseObject("400", "Thất bại", null);
        }
    }

    @GetMapping("/agent/transportation-schedules/find-schedule-by-scheduleId/{scheduleId}")
    private ResponseObject findScheduleByScheduleId(@PathVariable String scheduleId) {
        Map<String, Object> response = new HashMap<>();

        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(scheduleId);
        String price = ReplaceUtils.formatPrice(schedules.getUnitPrice());

        response.put("schedules", schedules);
        response.put("price", price);

        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/agent/transportation-schedules/find-transport-by-transportId/{transportId}")
    private ResponseObject findTransportByTransportId(@PathVariable String transportId) {
        Optional<Transportations> transportations = transportationService.findTransportById(transportId);

        if (transportations.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportations);
        }
    }

    @GetMapping("/agent/transportation-schedules/find-all-transport-by-transport-brandId/{transportBrandId}")
    private ResponseObject findAllTransportByBrandId(@PathVariable String transportBrandId) {
        List<Transportations> transportations = transportationService.findAllByTransportBrandId(transportBrandId);

        if (transportations.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportations);
        }
    }

    @GetMapping("/agent/transportation-schedules/check-duplicate-schedules")
    private ResponseObject checkDuplicateSchedule(@RequestParam("transportId") String transportId,
                                                  @RequestParam("departureTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                                                  @RequestParam("arrivalTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime) {
        Map<String, Object> response = null;
        List<TransportationSchedules> transportationSchedules = transportationScheduleService.findByTransportId(transportId);

        for (TransportationSchedules schedules : transportationSchedules) {
            response = new HashMap<>();
            LocalDateTime tripAStart = schedules.getDepartureTime().toLocalDateTime();
            LocalDateTime tripAEnd = schedules.getArrivalTime().toLocalDateTime();

            if (isOverlap(tripAStart, tripAEnd, departureTime, arrivalTime)) {
                response.put("exists", true);
                response.put("returnDate", schedules.getArrivalTime());
            } else {
                response.put("exists", false);
            }
        }

        if (transportationSchedules.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }

    @PostMapping("/agent/transportation-schedules/create-schedule")
    private void createSchedule(@RequestBody TransportationSchedulesDto scheduleDto) {
        String transportId = scheduleDto.getTransportationId();
        String scheduleId = GenerateNextID.generateNextCode("TSC", transportationScheduleService.findMaxCode());

        Optional<Transportations> transportationsOptional = transportationService.findTransportById(transportId);

        if (transportationsOptional.isPresent()) {
            Transportations transportations = transportationsOptional.get();
            int totalSeats = transportations.getAmountSeat();

            TransportationSchedules schedules = EntityDtoUtils.convertToEntity(scheduleDto, TransportationSchedules.class);
            schedules.setId(scheduleId);
            schedules.setDateCreated(new Timestamp(System.currentTimeMillis()));
            schedules.setIsActive(Boolean.TRUE);
            if (scheduleDto.getTripType()) {
                schedules.setIsStatus(4); // chuyến đi đang trống
                schedules.setBookedSeat(transportations.getAmountSeat());
            } else {
                schedules.setIsStatus(0); // chờ vận hành
                schedules.setBookedSeat(0);
            }
            transportationScheduleService.save(schedules);

            if (!scheduleDto.getTripType()) {
                for (int i = 1; i <= totalSeats; i++) {
                    TransportationScheduleSeats scheduleSeats = new TransportationScheduleSeats();
                    scheduleSeats.setSeatNumber(i);
                    scheduleSeats.setTransportationScheduleId(scheduleId);
                    scheduleSeats.setIsBooked(Boolean.FALSE);

                    transportScheduleSeatService.save(scheduleSeats);
                }
            }
        }
    }

    @PutMapping("/agent/transportation-schedules/update-schedule")
    private void updateSchedule(@RequestBody TransportationSchedulesDto scheduleDto) {
        String scheduleId = scheduleDto.getId();

        TransportationSchedules schedules = EntityDtoUtils.convertToEntity(scheduleDto, TransportationSchedules.class);
        schedules.setId(scheduleId);
        schedules.setUnitPrice(ReplaceUtils.replacePrice(scheduleDto.getPriceFormat()));
        transportationScheduleService.save(schedules);
    }

    @GetMapping("/agent/transportation-schedules/delete-schedule/{scheduleId}")
    private void deleteTrans(@PathVariable String scheduleId) {
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(scheduleId);
        schedules.setDateDeleted(new Timestamp(System.currentTimeMillis()));
        schedules.setIsActive(Boolean.FALSE);
        schedules.setIsStatus(3); // hủy chuyến đi
        transportationScheduleService.save(schedules);
    }

    private static boolean isOverlap(LocalDateTime startA, LocalDateTime endA, LocalDateTime startB, LocalDateTime endB) {
        boolean isStartBeforeEndA = startB.isBefore(endA) || startB.isEqual(endA);
        boolean isEndAfterStartA = endB.isAfter(startA) || endB.isEqual(startA);

        boolean isStartAfterEndA = startB.isAfter(endA) || startB.isEqual(endA);
        boolean isEndBeforeStartA = endB.isBefore(startA) || endB.isEqual(startA);

        return (isStartBeforeEndA && isEndAfterStartA) || (isStartAfterEndA && isEndBeforeStartA);
    }
}
