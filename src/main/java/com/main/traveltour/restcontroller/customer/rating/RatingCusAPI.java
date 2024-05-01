package com.main.traveltour.restcontroller.customer.rating;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.comment.UserCommentDto;
import com.main.traveltour.dto.customer.rating.RatingResponseDto;
import com.main.traveltour.entity.UserComments;
import com.main.traveltour.service.customer.UserCommentsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class RatingCusAPI {

    @Autowired
    private UserCommentsService userCommentsService;

    @PostMapping("customer/rating/rating-services/insert")
    public ResponseEntity<String> insertUserComments(@RequestBody UserCommentDto userCommentDto) {
        try {
            UserComments userComments = EntityDtoUtils.convertToEntity(userCommentDto, UserComments.class);
            userCommentsService.insertUserComments(userComments);
            return ResponseEntity.ok("{\"message\": \"Bạn đã đánh giá dịch vụ thành công\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("customer/rating/rating-services/update")
    public ResponseEntity<String> updateUserComments(@RequestBody UserCommentDto userCommentDto) {
        try {
            UserComments userComments = EntityDtoUtils.convertToEntity(userCommentDto, UserComments.class);
            userCommentsService.updateUserComments(userComments);
            return ResponseEntity.ok("{\"message\": \"Sửa đánh giá dịch vụ thành công\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("customer/rating/rating-services/delete")
    public ResponseEntity<String> deleteUserComments(@RequestParam Integer id) {
        try {
            userCommentsService.deleteUserComments(id);
            return ResponseEntity.ok("{\"message\": \"Xóa đánh giá dịch vụ thành công\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("customer/rating/rating-services/findCommentsByServiceId")
    public ResponseEntity<RatingResponseDto> findCommentsByServiceId(
            @RequestParam String serviceId,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false, defaultValue = "dateCreated") String sortBy,
            Pageable pageable) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return ResponseEntity.ok(userCommentsService.findCommentsByServiceId(serviceId, sortedPageable));

    }

    @GetMapping("customer/rating/rating-services/findUserCommentsById")
    public ResponseEntity<UserComments> findUserCommentsById(
            @RequestParam Integer id) {
        return ResponseEntity.ok(userCommentsService.findUserCommentsById(id));

    }

    @GetMapping("customer/rating/rating-services/findByOrderIdRating")
    public ResponseEntity<String> findByOrderIdRating(
            @RequestParam("orderId") String orderId) {
        try {
            Optional<UserComments> userCommentsOptional = userCommentsService.findByOrderIdRating(orderId);
            ObjectMapper objectMapper = new ObjectMapper();

            if (userCommentsOptional.isPresent()) {
                UserComments userComments = userCommentsOptional.get();
                String json = objectMapper.writeValueAsString(userComments);
                return ResponseEntity.ok().body(json);
            } else {
                return ResponseEntity.ok().body("");
            }
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("customer/rating/rating-services/findScoreRatingByTourId")
    public ResponseEntity<Double> findScoreRatingByTourId(@RequestParam("tourId") String tourId) {
        Double ratingScore = userCommentsService.findScoreRatingByRoomTypeId(tourId);
        return ResponseEntity.ok(ratingScore);
    }

    @GetMapping("customer/rating/rating-services/findCountRatingByTourId")
    public ResponseEntity<Integer> findCountRatingByTourId(@RequestParam("tourId") String tourId) {
        Integer countRating = userCommentsService.findCountRatingByRoomTypeId(tourId);
        return ResponseEntity.ok(countRating);
    }


}
