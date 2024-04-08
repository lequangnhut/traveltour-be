package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.UserComments;
import com.main.traveltour.entity.Users;
import com.main.traveltour.enums.CategoryService;
import com.main.traveltour.repository.*;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.customer.UserCommentsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserCommentsServiceImpl implements UserCommentsService {
    @Autowired
    private UserCommentsRepository userCommentsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ToursRepository toursRepository;
    @Autowired
    private HotelsRepository hotelsRepository;
    @Autowired
    private TransportationBrandsService transportationBrandsService;
    @Autowired
    private VisitLocationsService visitLocationsService;

    @Override
    public Page<UserComments> findCommentsByServiceId(String serviceId, Pageable pageable) {
        Objects.requireNonNull(serviceId, "Không có dữ liệu nào được tìm thấy");
        return userCommentsRepository.findAllByServiceId(serviceId, pageable);
    }

    @Override
    public UserComments findUserCommentsById(Integer id) {
        Objects.requireNonNull(id, "Không có dữ liệu nào được tìm thấy");
        return userCommentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Mã đánh giá không tồn tại " + id));
    }

    @Override
    public Page<UserComments> findUserCommentsByDateCreate(Timestamp dateCreate, Pageable pageable) {
        return userCommentsRepository.findAllByDateCreated(dateCreate, pageable);
    }

    @Override
    public Page<UserComments> findUserCommentsByStarAndServiceId(Integer start, String serviceId, Pageable pageable) {
        return userCommentsRepository.findAllByStarAndServiceId(start, serviceId, pageable);
    }

    @Override
    public void insertUserComments(UserComments userComments) {
        Objects.requireNonNull(userComments, "Không có dữ liệu nào được tìm thấy");

        Users customer = usersRepository.findById(userComments.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("Mã người dùng không tồn tại " + userComments.getUsersId()));
        Integer categoryId = getCategory(userComments.getServiceId());
        validateServiceExistence(categoryId, userComments.getServiceId());

        userCommentsRepository.save(UserComments.builder()
                .category(categoryId)
                .usersId(userComments.getUsersId())
                .serviceId(userComments.getServiceId())
                .star(userComments.getStar())
                .content(userComments.getContent())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .usersByUserId(customer)
                .build());
    }

    @Override
    public void updateUserComments(UserComments userComments) {
        Objects.requireNonNull(userComments, "Không có dữ liệu nào được tìm thấy");

        Users customer = usersRepository.findById(userComments.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("Mã người dùng không tồn tại " + userComments.getUsersId()));

        UserComments userComment = userCommentsRepository.findById(userComments.getId())
                .orElseThrow(() -> new IllegalArgumentException("Mã đánh giá không tồn tại " + userComments.getId()));

        Integer categoryId = getCategory(userComments.getServiceId());
        validateServiceExistence(categoryId, userComments.getServiceId());

        userCommentsRepository.save(UserComments.builder()
                .id(userComment.getId())
                .category(categoryId)
                .usersId(userComment.getUsersId())
                .serviceId(userComment.getServiceId())
                .star(userComment.getStar())
                .content(userComment.getContent())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .usersByUserId(customer)
                .build());
    }


    @Override
    @Transactional
    public void deleteUserComments(Integer commentId) {
        Objects.requireNonNull(commentId, "Không có dữ liệu nào được tìm thấy");

        UserComments userComment = userCommentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Mã đánh giá không tồn tại " + commentId));

        userCommentsRepository.delete(userComment);
    }

    private void validateServiceExistence(int categoryId, String serviceId) {
        switch (categoryId) {
            case 0:
                toursRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Tour không tồn tại " + serviceId));
                break;
            case 1:
                hotelsRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Khách sạn không tồn tại " + serviceId));
                break;
            case 2:
                transportationBrandsService.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại " + serviceId));
                break;
            case 3:
                visitLocationsService.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Địa điểm không tồn tại " + serviceId));
                break;
            default:
                throw new IllegalArgumentException("Dịch vụ không hợp lệ");
        }
    }

    private Integer getCategory(String serviceId) {
        String firstThreeLetters = serviceId.substring(0, 3);

        return switch (firstThreeLetters) {
            case "TBS" -> CategoryService.SERVICE_TOUR.getValue();
            case "HTL" -> CategoryService.SERVICE_HOTEL.getValue();
            case "TRP" -> CategoryService.SERVICE_TRANSPORT.getValue();
            case "PLA" -> CategoryService.SERVICE_LOCATION.getValue();
            default -> throw new IllegalArgumentException("Dịch vụ không hợp lệ");
        };
    }
}
