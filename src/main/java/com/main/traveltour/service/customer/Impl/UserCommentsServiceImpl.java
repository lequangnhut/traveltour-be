package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.dto.customer.rating.RatingResponseDto;
import com.main.traveltour.dto.customer.rating.UserCommentResponseDto;
import com.main.traveltour.dto.customer.rating.UserRatingDto;
import com.main.traveltour.entity.UserComments;
import com.main.traveltour.entity.Users;
import com.main.traveltour.enums.CategoryService;
import com.main.traveltour.repository.*;
import com.main.traveltour.service.agent.OrderVisitService;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.customer.UserCommentsService;
import com.main.traveltour.service.staff.BookingTourService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.staff.OrderTransportationService;
import com.main.traveltour.service.staff.impl.BookingToursServiceImpl;
import com.main.traveltour.utils.EntityDtoUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private OrderHotelsService orderHotelsService;
    @Autowired
    private OrderVisitService orderVisitService;
    @Autowired
    private OrderTransportationService orderTransportationService;
    @Autowired
    private BookingTourService bookingTourService;

    @Override
    public RatingResponseDto findCommentsByServiceId(String serviceId, Pageable pageable) {
        Objects.requireNonNull(serviceId, "{\"message\": \"Không có dữ liệu được tìm thấy\"}");

        // Lấy thông tin phân trang của đánh giá
        Page<UserComments> userCommentsPage = userCommentsRepository.findAllByServiceId(serviceId, pageable);
        Page<UserComments> updatedUserCommentsPage = userCommentsPage.map(userComment -> {
            Users user = usersRepository.findById(userComment.getUsersId())
                    .orElseThrow(() -> new IllegalStateException("{\"message\": \"Khách hàng không tồn tại\"}"));
            UserRatingDto.builder().build();
            userComment.setUsersByUserId(user);
            return userComment;
        });
        Page<UserCommentResponseDto> userCommentPage = EntityDtoUtils.convertPage(updatedUserCommentsPage, UserCommentResponseDto.class);
        // Lấy thông tin tất cả điểm đánh giá sản phẩm
        List<Integer> starRatings = userCommentsRepository.findAllByServiceId(serviceId)
                .stream()
                .mapToInt(UserComments::getStar)
                .boxed()
                .toList();

        // Lấy thông tin trung bình tổng điểm đánh giá
        double averageRating = starRatings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double roundedAverageRating = Math.round(averageRating * 10.0) / 10.0;
        int numberOfRatings = starRatings.size();

        return RatingResponseDto.builder()
                .userComments(userCommentPage)
                .roundedAverageRating(roundedAverageRating)
                .numberOfRatings(numberOfRatings)
                .build();
    }

    @Override
    public UserComments findUserCommentsById(Integer id) {
        Objects.requireNonNull(id, "{\"message\": \"Không có dữ liệu được tìm thấy\"}");
        return userCommentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Đánh giá không tồn tại " + id + "\"}"));
    }

    @Override
    public Optional<UserComments> findByOrderIdRating(String orderId) {
        return Optional.ofNullable(orderId)
                .filter(id -> !id.isEmpty())
                .map(id -> userCommentsRepository.findByOrderId(id))
                .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Không có dữ liệu được tìm thấy\"}"));
    }



    @Override
    public Double findScoreRatingByRoomTypeId(String serviceId) {
        List<UserComments> userComments = userCommentsRepository.findAllByServiceId(serviceId);
        double ratingScore = 0.0;

        if (userComments.isEmpty()) {
            return ratingScore;
        } else {
            for (var user : userComments) {
                ratingScore += user.getStar();
            }

            double averageRating = ratingScore / userComments.size();

            DecimalFormat df = new DecimalFormat("#.#");
            return Double.valueOf(df.format(averageRating));
        }
    }



    @Override
    public Page<UserComments> findUserCommentsByStarAndServiceId(Integer start, String serviceId, Pageable pageable) {
        return userCommentsRepository.findAllByStarAndServiceId(start, serviceId, pageable);
    }

    @Override
    public void insertUserComments(UserComments userComments) {
        Objects.requireNonNull(userComments, "{\"message\": \"Không có dữ liệu nào được tìm thấy\"}");

        Users customer = usersRepository.findById(userComments.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Không tìm thấy thông tin khách hàng " + userComments.getUsersId() + "\"}"));

        Integer categoryId = getCategory(userComments.getServiceId());

        validateServiceExistence(categoryId, userComments.getServiceId());
        validateOrderServiceExistence(categoryId, userComments.getOrderId());

        userCommentsRepository.save(UserComments.builder()
                .category(categoryId)
                .usersId(userComments.getUsersId())
                .serviceId(userComments.getServiceId())
                .star(userComments.getStar())
                .content(userComments.getContent())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .usersByUserId(customer)
                .orderId(userComments.getOrderId())
                .build());
    }

    @Override
    public void updateUserComments(UserComments userComments) {
        Objects.requireNonNull(userComments, "{\"message\": \"Không có dữ liệu nào được tìm thấy\"}");

        Users customer = usersRepository.findById(userComments.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Không tìm thấy thông tin khách hàng " + userComments.getUsersId() + "\"}"));

        UserComments userComment = userCommentsRepository.findById(userComments.getId())
                .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Đánh giá không tồn tại " + userComments.getId() + "\"}"));

        Integer categoryId = getCategory(userComments.getServiceId());
        validateServiceExistence(categoryId, userComments.getServiceId());

        userCommentsRepository.save(UserComments.builder()
                .id(userComment.getId())
                .category(categoryId)
                .usersId(userComment.getUsersId())
                .serviceId(userComment.getServiceId())
                .star(userComments.getStar())
                .content(userComments.getContent())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .usersByUserId(customer)
                .orderId(userComment.getOrderId())
                .build());
    }


    @Override
    @Transactional
    public void deleteUserComments(Integer commentId) {
        Objects.requireNonNull(commentId, "{\"message\": \"Không có dữ liệu nào được tìm thấy\"}");

        UserComments userComment = userCommentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Đánh giá không tồn tại " + commentId + "\"}"));

        userCommentsRepository.delete(userComment);
    }

    @Override
    public List<UserComments> findAllUsersComments() {
        return userCommentsRepository.findAll();
    }

    @Override
    public Integer findCountRatingByRoomTypeId(String id) {
        List<UserComments> userComments = userCommentsRepository.findAllByServiceId(id);
        return userComments == null ? 0 : userComments.size();
    }

    private void validateServiceExistence(int categoryId, String serviceId) {
        switch (categoryId) {
            case 0:
                toursRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Tour không tồn tại " + serviceId +"\"}"));
                break;
            case 1:
                hotelsRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Khách sạn không tồn tại " + serviceId +"\"}"));
                break;
            case 2:
                transportationBrandsService.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Xe không tồn tại " + serviceId +"\"}"));
                break;
            case 3:
                visitLocationsService.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Địa điểm không tồn tại " + serviceId +"\"}" + serviceId));
                break;
            default:
                throw new IllegalArgumentException("{\"message\": \"Dịch vụ không hợp lệ \"}");
        }
    }

    private void validateOrderServiceExistence(int categoryId, String serviceId) {
        switch (categoryId) {
            case 0:
                bookingTourService.findByIdOptional(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Tour không tồn tại " + serviceId +"\"}"));
                break;
            case 1:
                orderHotelsService.findByIdOptional(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Khách sạn không tồn tại " + serviceId +"\"}"));
                break;
            case 2:
                orderTransportationService.findByIdOptional(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Xe không tồn tại " + serviceId +"\"}"));
                break;
            case 3:
                orderVisitService.findByIdOptional(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("{\"message\": \"Địa điểm không tồn tại " + serviceId +"\"}" + serviceId));
                break;
            default:
                throw new IllegalArgumentException("{\"message\": \"Dịch vụ không hợp lệ \"}");
        }
    }

    private Integer getCategory(String serviceId) {
        String firstThreeLetters = serviceId.substring(0, 3);

        return switch (firstThreeLetters) {
            case "TR0" -> CategoryService.SERVICE_TOUR.getValue();
            case "HTL" -> CategoryService.SERVICE_HOTEL.getValue();
            case "TRP" -> CategoryService.SERVICE_TRANSPORT.getValue();
            case "PLA" -> CategoryService.SERVICE_LOCATION.getValue();
            default -> throw new IllegalArgumentException("{\"message\": \"Dịch vụ không hợp lệ \"}");
        };
    }
}
