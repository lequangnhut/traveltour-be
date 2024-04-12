package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.UsersDto;
import com.main.traveltour.dto.customer.infomation.ChangePassDto;
import com.main.traveltour.dto.staff.CustomerDto;
import com.main.traveltour.dto.staff.tour.ToursDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.RolesService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/super-admin/account-customer/")
public class CustomerAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private FileUpload fileUpload;

    @GetMapping("find-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        try {
            Users user = usersService.findById(id);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", EntityDtoUtils.convertToDto(user, UsersDto.class));
        } catch (Exception e) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("find-role-customer")
    private ResponseObject getListUserRoleCustomer(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir,
                                                   @RequestParam(required = false) String searchTerm) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<Users> users = searchTerm == null || searchTerm.isEmpty()
                    ? usersService.findDecentralizationCustomer(PageRequest.of(page, size, sort))
                    : usersService.findAllAccountCustomerWithSearch(searchTerm, PageRequest.of(page, size, sort));
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", users);
        } catch (Exception e) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @PostMapping("create-account-customer")
    private ResponseObject createAccountCustomer
            (@RequestPart CustomerDto customerDto,
             @RequestPart("customerAvatar") MultipartFile customerAvatar) {
        try {
            String imagesPath = fileUpload.uploadFile(customerAvatar);
            Users user = EntityDtoUtils.convertToEntity(customerDto, Users.class);

            List<Roles> roles = new ArrayList<>();
            Roles customerRole = rolesService.findByNameRole("ROLE_CUSTOMER");
            roles.add(customerRole);

            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            user.setAvatar(imagesPath);
            user.setAddress("Việt Nam");
            user.setDateCreated(new Timestamp(System.currentTimeMillis()));
            usersService.save(user);
            return new ResponseObject("200", "Thêm thành công", user);
        } catch (Exception e) {
            return new ResponseObject("404", "Thêm thất bại", null);
        }
    }

    @PutMapping("update-account-customer/{id}")
    public ResponseObject updateAccountCustomerById
            (@PathVariable int id,
             @RequestPart CustomerDto customerDto,
             @RequestPart(required = false) MultipartFile customerAvatar) {
        try {
            Users existingUser = usersService.findById(id);
            String currentImagePath = existingUser.getAvatar();

            Users user = usersService.findById(id);
            user.setGender(customerDto.getGender());
            user.setFullName(customerDto.getFullName());
            user.setBirth(customerDto.getBirth());
            user.setPhone(customerDto.getPhone());
            user.setCitizenCard(customerDto.getCitizenCard());
            user.setAddress(customerDto.getAddress());
            user.setIsActive(customerDto.getIsActive());
            if (customerAvatar != null) {
                String imagesPath = fileUpload.uploadFile(customerAvatar);
                user.setAvatar(imagesPath);
            } else {
                user.setAvatar(currentImagePath);
            }
            usersService.save(user);
            return new ResponseObject("200", "Cập nhật thành công", user);
        } catch (Exception e) {
            return new ResponseObject("404", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("delete-account-customer/{id}")
    private ResponseObject deleteAccountCustomerById(@PathVariable int id) {
        try {
            Users users = usersService.findById(id);
            users.setIsActive(Boolean.FALSE);
            usersService.save(users);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", true);
        } catch (Exception e) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", false);
        }

    }

    @PutMapping("update-customer-phone/{id}&{phone}")
    public ResponseObject updatePhone(@PathVariable int id, @PathVariable String phone) {
        try {
            Users users = usersService.findById(id);
            users.setPhone(phone);
            Users updatePassUser = usersService.save(users);
            //Thành công thì loại luôn token đó
            return new ResponseObject("200", "Cập nhật thành công", updatePassUser);
        } catch (Exception e) {
            return new ResponseObject("404", "Cập nhật thất bại", null);
        }
    }

    @PutMapping("update-pass/{id}")
    public ResponseObject updatePasswordAPI(@PathVariable int id, @RequestBody ChangePassDto changePassDto) {
        try {
            Users users = usersService.findById(id);
            //get link hình
            //String imagePath = users.getAvatar();
            //Lưu mật khẩu mới vào db
            String passwordEncore = passwordEncoder.encode(changePassDto.getNewPass());
            users.setPassword(passwordEncore);
            //users.setAvatar(imagePath);
            Users updatePassUser = usersService.save(users);

            return new ResponseObject("200", "Cập nhật thành công", updatePassUser);
        } catch (Exception e) {
            return new ResponseObject("404", "Cập nhật thất bại", null);
        }
    }

    @GetMapping("check-current-password/{id}&{currentPass}")
    public Map<String, Boolean> checkCurrentPassAPI(@PathVariable int id, @PathVariable String currentPass) {
        Users users = usersService.findById(id);
        Map<String, Boolean> response = new HashMap<>();

        if (users != null) {
            boolean isCurrentPassCorrect = passwordEncoder.matches(currentPass, users.getPassword());
            if (isCurrentPassCorrect) {
                response.put("exists", true);
            } else {
                response.put("exists", false);
            }
        }
        return response;
    }
}
