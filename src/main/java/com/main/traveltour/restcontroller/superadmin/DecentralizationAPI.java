package com.main.traveltour.restcontroller.superadmin;

import com.main.traveltour.entity.*;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class DecentralizationAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private AgenciesService agenciesService;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @GetMapping("superadmin/decentralization/find-role-staff")
    private ResponseEntity<Page<Users>> getListUserRoleStaff(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String sortDir,
                                                             @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Users> users = searchTerm == null || searchTerm.isEmpty() ? usersService.findDecentralizationStaff(PageRequest.of(page, size, sort)) : usersService.findAllAccountStaffWithSearch(searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("superadmin/decentralization/find-role-agent")
    private ResponseEntity<Page<Users>> getListUserRoleAgent(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String sortDir,
                                                             @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Users> users = searchTerm == null || searchTerm.isEmpty() ? usersService.findDecentralizationAgent(PageRequest.of(page, size, sort)) : usersService.findAllAccountAgentWithSearch(searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("superadmin/decentralization/update-role/{userId}")
    private ResponseEntity<?> updateRoles(@PathVariable int userId, @RequestBody List<String> roles) {
        Users user = usersService.findById(userId);

        List<Roles> rolesList = roles.stream().map(rolesService::findByNameRole).collect(Collectors.toList());
        user.setRoles(rolesList);

        if (roles.stream().anyMatch(role -> role.startsWith("ROLE_AGENT"))) {
            changeBusiness(userId, roles);
        }

        Users updatedUser = usersService.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    private void changeBusiness(int userId, List<String> roles) {
        String hotelId = GenerateNextID.generateNextCode("HTL", hotelsService.findMaxCode());
        String transId = GenerateNextID.generateNextCode("TRP", transportationBrandsService.findMaxCode());
        String placeId = GenerateNextID.generateNextCode("PLA", visitLocationsService.findMaxCode());

        Agencies agencies = agenciesService.findByUserId(userId);
        List<Hotels> hotels = hotelsService.findAllByAgencyId(agencies.getId());
        List<TransportationBrands> transportationBrands = transportationBrandsService.findAllByAgencyId(agencies.getId());
        List<VisitLocations> visitLocations = visitLocationsService.findAllByAgencyId(agencies.getId());

        if (hotels != null) {
            for (Hotels hotel : hotels) {
                hotel.setIsActive(roles.contains("ROLE_AGENT_HOTEL"));
                hotelsService.save(hotel);
            }
        } else if (roles.contains("ROLE_AGENT_HOTEL")) {
            Hotels newHotel = new Hotels();
            newHotel.setId(hotelId);
            newHotel.setHotelTypeId(1);
            newHotel.setAgenciesId(agencies.getId());
            newHotel.setIsAccepted(Boolean.FALSE);
            newHotel.setIsActive(Boolean.TRUE);
            newHotel.setDateCreated(new Timestamp(System.currentTimeMillis()));
            hotelsService.save(newHotel);
        }

        if (transportationBrands != null) {
            for (TransportationBrands brands : transportationBrands) {
                brands.setIsActive(roles.contains("ROLE_AGENT_TRANSPORT"));
                transportationBrandsService.save(brands);
            }
        } else if (roles.contains("ROLE_AGENT_TRANSPORT")) {
            TransportationBrands newTransportationBrand = new TransportationBrands();
            newTransportationBrand.setId(transId);
            newTransportationBrand.setAgenciesId(agencies.getId());
            newTransportationBrand.setIsAccepted(Boolean.FALSE);
            newTransportationBrand.setIsActive(Boolean.TRUE);
            newTransportationBrand.setDateCreated(new Timestamp(System.currentTimeMillis()));
            transportationBrandsService.save(newTransportationBrand);
        }

        if (visitLocations != null) {
            for (VisitLocations locations : visitLocations) {
                locations.setIsActive(roles.contains("ROLE_AGENT_PLACE"));
                visitLocationsService.save(locations);
            }
        } else if (roles.contains("ROLE_AGENT_PLACE")) {
            VisitLocations newVisitLocation = new VisitLocations();
            newVisitLocation.setId(placeId);
            newVisitLocation.setVisitLocationTypeId(1);
            newVisitLocation.setAgenciesId(agencies.getId());
            newVisitLocation.setIsAccepted(Boolean.FALSE);
            newVisitLocation.setIsActive(Boolean.TRUE);
            newVisitLocation.setDateCreated(new Timestamp(System.currentTimeMillis()));
            visitLocationsService.save(newVisitLocation);
        }
    }
}
