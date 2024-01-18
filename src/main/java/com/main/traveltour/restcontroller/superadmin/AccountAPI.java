package com.main.traveltour.restcontroller.superadmin;

import com.main.traveltour.dto.UsersDto;
import com.main.traveltour.dto.superadmin.AccountDto;
import com.main.traveltour.dto.superadmin.DataAccount;
import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AccountAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private AgenciesService agenciesService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("superadmin/account/find-all-account-staff")
    private ResponseEntity<Page<Users>> findAllAccountStaff(@RequestParam(defaultValue = "0") int page) {
        Page<Users> items = usersService.findAllAccountStaff(PageRequest.of(page, 10));
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("superadmin/account/find-all-account-agent")
    private ResponseEntity<Page<Users>> findAllAccountAgent(@RequestParam(defaultValue = "0") int page) {
        Page<Users> items = usersService.findAllAccountAgent(PageRequest.of(page, 10));
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("superadmin/account/find-by-id/{id}")
    private UsersDto findById(@PathVariable int id) {
        Users user = usersService.findById(id);
        return EntityDtoUtils.convertToDto(user, UsersDto.class);
    }

    @PostMapping("superadmin/account/create-account")
    private void createAccount(@RequestBody DataAccount dataAccount) {
        AccountDto accountDto = dataAccount.getAccountDto();
        Users user = EntityDtoUtils.convertToEntity(accountDto, Users.class);

        List<String> roles = dataAccount.getRoles();
        List<Roles> rolesList = roles.stream()
                .map(rolesService::findByNameRole)
                .collect(Collectors.toList());

        user.setRoles(rolesList);
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setAddress("Việt Nam");
        user.setDateCreated(new Timestamp(System.currentTimeMillis()));
        usersService.save(user);

        boolean containsAgentRole = roles.stream().anyMatch(role -> role.contains("ROLE_AGENT"));

        if (containsAgentRole) {
            Agencies agencies = new Agencies();
            agencies.setUserId(user.getId());
            agencies.setDateCreated(new Timestamp(System.currentTimeMillis()));
            agencies.setIsActive(Boolean.TRUE);
            agencies.setIsAccepted(0); // 0 là chưa kích hoạt, 1 chờ kích hoạt, 2 kích hoạt thành công, 3 kích hoạt thất bại
            agenciesService.save(agencies);
        }
    }

    @PutMapping("superadmin/account/update-account")
    private void updateAccount(@RequestBody AccountDto accountDto) {
        Users users = usersService.findById(accountDto.getId());
        users.setFullName(accountDto.getFullName());
        users.setPhone(accountDto.getPhone());
        users.setIsActive(accountDto.getIsActive());
        users.setCitizenCard(accountDto.getCitizenCard());
        users.setAddress(accountDto.getAddress());
        usersService.save(users);
    }

    @DeleteMapping("superadmin/account/delete-account/{id}")
    private void deleteAccount(@PathVariable int id) {
        Users users = usersService.findById(id);
        users.setIsActive(Boolean.FALSE);
        usersService.save(users);
    }
}
