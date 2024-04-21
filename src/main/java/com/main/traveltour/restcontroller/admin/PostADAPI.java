package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.type.TransportUtilitiesDto;
import com.main.traveltour.dto.admin.post.*;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.*;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.agent.TransportationService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/admin/post/")
public class PostADAPI {

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @Autowired
    private RoomTypesServiceAD roomTypesServiceAD;

    @Autowired
    private TransServiceAD transServiceAD;

    @Autowired
    private VisitLocationsServiceAD visitLocationsServiceAD;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private TransportationService transportationService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportUtilityServiceAD transportUtilityServiceAD;

    @GetMapping("all-hotel-post")
    private ResponseObject findAllHotelPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Hotels> items = searchTerm == null || searchTerm.isEmpty()
                ? hotelsServiceAD.findAllHotelPost(isAccepted, PageRequest.of(page, size, sort))
                : hotelsServiceAD.findAllHotelPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);

        Page<HotelsDto> hotelsDtos = items.map(hotels -> EntityDtoUtils.convertToDto(hotels, HotelsDto.class));

        if (hotelsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelsDtos);
        }
    }

    @GetMapping("find-room-by-hotelId/{hotelId}")
    public ResponseObject findRoomByHotel(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          @RequestParam(required = false) String searchTerm,
                                          @RequestParam(required = false) Integer isActive,
                                          @PathVariable String hotelId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<RoomTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? roomTypesServiceAD.findByHotelId(isActive, hotelId, PageRequest.of(page, size, sort))
                : roomTypesServiceAD.findByHotelIdAndName(isActive, hotelId, PageRequest.of(page, size, sort), searchTerm);

        Page<RoomTypesDto> roomTypesDtos = items.map(roomTypes -> EntityDtoUtils.convertToDto(roomTypes, RoomTypesDto.class));

        if (roomTypesDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", roomTypesDtos);
        }
    }

    @GetMapping("all-brand-post")
    private ResponseObject findAllBrandPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TransportationBrands> items = searchTerm == null || searchTerm.isEmpty()
                ? transServiceAD.findAllBrandPost(isAccepted, PageRequest.of(page, size, sort))
                : transServiceAD.findAllBrandPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);

        Page<TransportationBrandsDto> transportationBrandsDtos = items.map(transportationBrands -> EntityDtoUtils.convertToDto(transportationBrands, TransportationBrandsDto.class));

        if (transportationBrandsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationBrandsDtos);
        }
    }

    @GetMapping("find-trans-by-brandId/{brandId}")
    public ResponseObject findTransByBrandId(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortDir,
                                             @RequestParam(required = false) String searchTerm,
                                             @RequestParam(required = false) Boolean isActive,
                                             @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Transportations> items = searchTerm == null || searchTerm.isEmpty()
                ? transServiceAD.findAllTransPost(isActive, brandId, PageRequest.of(page, size, sort))
                : transServiceAD.findAllTransPostByName(isActive, brandId, PageRequest.of(page, size, sort), searchTerm);

        Page<TransportationsDto> transportationsDto = items.map(transportations -> EntityDtoUtils.convertToDto(transportations, TransportationsDto.class));

        if (transportationsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationsDto);
        }
    }

    @GetMapping("find-trip-by-transId/{transId}")
    public ResponseObject findTripByTransId(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isActive,
                                            @PathVariable String transId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TransportationSchedules> items = transServiceAD.findAllSchedulesPostByName(isActive, transId, PageRequest.of(page, size, sort), searchTerm);
        Page<TransportationSchedulesDto> transportationSchedulesDtos = items.map(transportationSchedules -> EntityDtoUtils.convertToDto(transportationSchedules, TransportationSchedulesDto.class));

        if (transportationSchedulesDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);
        }
    }

    @GetMapping("all-visit-post")
    private ResponseObject findAllVisitPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<VisitLocations> items = searchTerm == null || searchTerm.isEmpty()
                ? visitLocationsServiceAD.findAllVisitPost(isAccepted, PageRequest.of(page, size, sort))
                : visitLocationsServiceAD.findAllVisitPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);
        Page<VisitLocationsDto> visitLocationsDtos = items.map(visitLocations -> EntityDtoUtils.convertToDto(visitLocations, VisitLocationsDto.class));

        if (visitLocationsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDtos);
        }
    }

    @PutMapping("denied-hotel-post/{id}")
    public ResponseObject deniedForHotel(@PathVariable String id) {
        try {
            Hotels hotels = hotelsServiceAD.findById(id);
            if (hotels != null) {
                hotels.setIsAccepted(false);
                hotelsServiceAD.save(hotels);
                return new ResponseObject("200", "Có nè", hotels);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-hotel-post/{id}")
    public ResponseObject acceptedForHotel(@PathVariable String id) {
        try {
            Hotels hotels = hotelsServiceAD.findById(id);
            if (hotels != null) {
                hotels.setIsAccepted(true);
                hotelsServiceAD.save(hotels);
                return new ResponseObject("200", "Có nè", hotels);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("denied-room-post/{id}")
    public ResponseObject deniedForRoom(@PathVariable String id) {
        try {
            RoomTypes roomTypes = roomTypesServiceAD.findById(id);
            if (roomTypes != null) {
                roomTypes.setIsActive(0);
                roomTypesServiceAD.save(roomTypes);
                return new ResponseObject("200", "Có nè", roomTypes);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-room-post/{id}")
    public ResponseObject acceptedForRoom(@PathVariable String id) {
        try {
            RoomTypes roomTypes = roomTypesServiceAD.findById(id);
            if (roomTypes != null) {
                roomTypes.setIsActive(1);
                roomTypesServiceAD.save(roomTypes);
                return new ResponseObject("200", "Có nè", roomTypes);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("denied-brand-post/{id}")
    public ResponseObject deniedForBrand(@PathVariable String id) {
        try {
            TransportationBrands transportationBrands = transServiceAD.findByBrandId(id);
            if (transportationBrands != null) {
                transportationBrands.setIsAccepted(false);
                transportationBrandsService.save(transportationBrands);
                return new ResponseObject("200", "Có nè", transportationBrands);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-brand-post/{id}")
    public ResponseObject acceptedForBrand(@PathVariable String id) {
        try {
            TransportationBrands transportationBrands = transServiceAD.findByBrandId(id);
            if (transportationBrands != null) {
                transportationBrands.setIsAccepted(true);
                transportationBrandsService.save(transportationBrands);
                return new ResponseObject("200", "Có nè", transportationBrands);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("denied-trans-post/{id}")
    public ResponseObject deniedForTrans(@PathVariable String id) {
        try {
            Transportations transportations = transServiceAD.findByTransId(id);
            if (transportations != null) {
                transportations.setIsActive(false);
                transportationService.save(transportations);
                return new ResponseObject("200", "Có nè", transportations);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-trans-post/{id}")
    public ResponseObject acceptedForTrans(@PathVariable String id) {
        try {
            Transportations transportations = transServiceAD.findByTransId(id);
            if (transportations != null) {
                transportations.setIsActive(true);
                transportationService.save(transportations);
                return new ResponseObject("200", "Có nè", transportations);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("denied-schedule-post/{id}")
    public ResponseObject deniedForSchedules(@PathVariable String id) {
        try {
            TransportationSchedules transportationSchedules = transServiceAD.findByScheduleId(id);
            if (transportationSchedules != null) {
                transportationSchedules.setIsActive(false);
                transportationScheduleService.save(transportationSchedules);
                return new ResponseObject("200", "Có nè", transportationSchedules);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-schedule-post/{id}")
    public ResponseObject acceptedForSchedules(@PathVariable String id) {
        try {
            TransportationSchedules transportationSchedules = transServiceAD.findByScheduleId(id);
            if (transportationSchedules != null) {
                transportationSchedules.setIsActive(true);
                transportationScheduleService.save(transportationSchedules);
                return new ResponseObject("200", "Có nè", transportationSchedules);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("denied-visit-post/{id}")
    public ResponseObject deniedForVisit(@PathVariable String id) {
        try {
            VisitLocations visitLocations = visitLocationsServiceAD.findById(id);
            if (visitLocations != null) {
                visitLocations.setIsAccepted(false);
                visitLocationsServiceAD.save(visitLocations);
                return new ResponseObject("200", "Có nè", visitLocations);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @PutMapping("accepted-visit-post/{id}")
    public ResponseObject acceptedForVisit(@PathVariable String id) {
        try {
            VisitLocations visitLocations = visitLocationsServiceAD.findById(id);
            if (visitLocations != null) {
                visitLocations.setIsAccepted(true);
                visitLocationsServiceAD.save(visitLocations);
                return new ResponseObject("200", "Có nè", visitLocations);
            } else {
                return new ResponseObject("500", "Hông nè", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Hông nè", null);
        }
    }

    @GetMapping("find-trans-utility/{id}")
    private ResponseObject findAllUtilityByTransId(@PathVariable String id) {

        List<TransportUtilities> utilities = transportUtilityServiceAD.findAllByTransId(id);
        List<TransportUtilitiesDto> transportUtilitiesDto = EntityDtoUtils.convertToDtoList(utilities, TransportUtilitiesDto.class);

        if (transportUtilitiesDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportUtilitiesDto);
        }
    }
}
