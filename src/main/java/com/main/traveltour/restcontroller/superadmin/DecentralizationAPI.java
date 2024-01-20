package com.main.traveltour.restcontroller.superadmin;

import com.main.traveltour.entity.*;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @Autowired
    private HotelsService hotelsService;

    @GetMapping("superadmin/decentralization/find-role-staff")
    private ResponseEntity<Page<Users>> getListUserRoleStaff(@RequestParam(defaultValue = "0") int page) {
        Page<Users> users = usersService.findDecentralizationStaffByActiveIsTrue(PageRequest.of(page, 10));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("superadmin/decentralization/find-role-agent")
    private ResponseEntity<Page<Users>> getListUserRoleAgent(@RequestParam(defaultValue = "0") int page) {
        Page<Users> users = usersService.findDecentralizationAgentByActiveIsTrue(PageRequest.of(page, 10));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("superadmin/decentralization/update-role/{userId}")
    private ResponseEntity<?> updateRoles(@PathVariable int userId, @RequestBody List<String> roles) {
        Users user = usersService.findById(userId);

        List<Roles> rolesList = roles.stream().map(rolesService::findByNameRole).collect(Collectors.toList());
        user.setRoles(rolesList);

        changeBusiness(userId, roles);

        Users updatedUser = usersService.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    private void changeBusiness(int userId, List<String> roles) {
        Agencies agencies = agenciesService.findByUserId(userId);
        Hotels hotels = hotelsService.findByAgencyId(agencies.getId());
        TransportationBrands transportationBrands = transportationBrandsService.findByAgencyId(agencies.getId());
        VisitLocations visitLocations = visitLocationsService.findByAgencyId(agencies.getId());

        if (hotels != null) {
            hotels.setIsActive(roles.contains("ROLE_AGENT_HOTEL"));
            hotelsService.save(hotels);
        } else if (roles.contains("ROLE_AGENT_HOTEL")) {
            Hotels newHotel = new Hotels();
            newHotel.setHotelTypeId(1);
            newHotel.setAgenciesId(agencies.getId());
            newHotel.setIsAccepted(Boolean.FALSE);
            newHotel.setIsActive(Boolean.TRUE);
            newHotel.setDateCreated(new Timestamp(System.currentTimeMillis()));
            hotelsService.save(newHotel);
        }

        if (transportationBrands != null) {
            transportationBrands.setIsActive(roles.contains("ROLE_AGENT_TRANSPORT"));
            transportationBrandsService.save(transportationBrands);
        } else if (roles.contains("ROLE_AGENT_TRANSPORT")) {
            TransportationBrands newTransportationBrand = new TransportationBrands();
            newTransportationBrand.setAgenciesId(agencies.getId());
            newTransportationBrand.setIsAccepted(Boolean.FALSE);
            newTransportationBrand.setIsActive(Boolean.TRUE);
            newTransportationBrand.setDateCreated(new Timestamp(System.currentTimeMillis()));
            transportationBrandsService.save(newTransportationBrand);
        }

        if (visitLocations != null) {
            visitLocations.setIsActive(roles.contains("ROLE_AGENT_PLACE"));
            visitLocationsService.save(visitLocations);
        } else if (roles.contains("ROLE_AGENT_PLACE")) {
            VisitLocations newVisitLocation = new VisitLocations();
            newVisitLocation.setVisitLocationTypeId(1);
            newVisitLocation.setAgenciesId(agencies.getId());
            newVisitLocation.setIsAccepted(Boolean.FALSE);
            newVisitLocation.setIsActive(Boolean.TRUE);
            newVisitLocation.setDateCreated(new Timestamp(System.currentTimeMillis()));
            visitLocationsService.save(newVisitLocation);
        }
    }
}
