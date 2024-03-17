package com.main.traveltour.restcontroller.customer.registeragency;

import com.main.traveltour.dto.customer.infomation.ForgotPasswordDto;
import com.main.traveltour.dto.superadmin.AccountDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.customer.PassOTPService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class RegisterAgenciesCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private PassOTPService passOTPService;

    @Autowired
    private AgenciesService agenciesService;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("customer/agencies/submit-email")
    public ResponseObject submitOTP(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        emailService.queueEmailOTPCus(forgotPasswordDto);

        PassOTP passOTP = new PassOTP();
        passOTP.setUsersId(1);
        passOTP.setEmail(forgotPasswordDto.getEmail());
        passOTP.setCodeToken(forgotPasswordDto.getVerifyCode());
        passOTP.setIsActive(true);
        passOTP.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        passOTPService.save(passOTP);

        return new ResponseObject("200", "Đã tìm thấy dữ liệu", forgotPasswordDto);
    }

    @GetMapping("customer/agencies/find-by-otp/{codeOtp}/{email}")
    public ResponseObject findByOTPAndEmail(@PathVariable String codeOtp, @PathVariable String email) {
        PassOTP passOTP = passOTPService.findByOTPAndEmail(codeOtp, email);

        if (passOTP != null && passOTP.getIsActive()) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", passOTP);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @PostMapping("customer/agencies/register-agencies")
    public ResponseObject registerAgencies(@RequestBody DataAccountDto dataAccountDto) {
        AccountDto accountDto = dataAccountDto.getAccountDto();
        Users user = EntityDtoUtils.convertToEntity(accountDto, Users.class);

        List<String> roles = dataAccountDto.getRoles();
        List<Roles> rolesList = roles.stream().map(rolesService::findByNameRole).collect(Collectors.toList());

        user.setRoles(rolesList);
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setAddress("Việt Nam");
        user.setIsActive(Boolean.TRUE);
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

            registerBusiness(agencies.getId(), roles);

            emailService.queueEmailCreateBusiness(dataAccountDto);
        }

        if (accountDto == null) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", "Thất bại");
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", "Thành công");
        }
    }

    private void registerBusiness(int agenciesId, List<String> roles) {
        String hotelId = GenerateNextID.generateNextCode("HTL", hotelsService.findMaxCode());
        String transId = GenerateNextID.generateNextCode("TRP", transportationBrandsService.findMaxCode());
        String placeId = GenerateNextID.generateNextCode("PLA", visitLocationsService.findMaxCode());

        if (roles.contains("ROLE_AGENT_HOTEL")) {
            Hotels hotels = new Hotels();
            hotels.setId(hotelId);
            hotels.setHotelTypeId(1);
            hotels.setAgenciesId(agenciesId);
            hotels.setIsAccepted(Boolean.FALSE);
            hotels.setIsActive(Boolean.TRUE);
            hotels.setDateCreated(new Timestamp(System.currentTimeMillis()));
            hotelsService.save(hotels);
        }
        if (roles.contains("ROLE_AGENT_TRANSPORT")) {
            TransportationBrands transportationBrands = new TransportationBrands();
            transportationBrands.setId(transId);
            transportationBrands.setAgenciesId(agenciesId);
            transportationBrands.setIsAccepted(Boolean.FALSE);
            transportationBrands.setIsActive(Boolean.TRUE);
            transportationBrands.setDateCreated(new Timestamp(System.currentTimeMillis()));
            transportationBrandsService.save(transportationBrands);
        }
        if (roles.contains("ROLE_AGENT_PLACE")) {
            VisitLocations visitLocations = new VisitLocations();
            visitLocations.setId(placeId);
            visitLocations.setVisitLocationTypeId(1);
            visitLocations.setAgenciesId(agenciesId);
            visitLocations.setIsAccepted(Boolean.FALSE);
            visitLocations.setIsActive(Boolean.TRUE);
            visitLocations.setDateCreated(new Timestamp(System.currentTimeMillis()));
            visitLocationsService.save(visitLocations);
        }
    }
}
