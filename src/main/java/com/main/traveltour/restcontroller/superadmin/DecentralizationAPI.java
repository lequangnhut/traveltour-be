package com.main.traveltour.restcontroller.superadmin;

import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        List<Roles> rolesList = roles.stream()
                .map(rolesService::findByNameRole)
                .collect(Collectors.toList());
        user.setRoles(rolesList);

        Users updatedUser = usersService.save(user);
        return ResponseEntity.ok(updatedUser);
    }
}
