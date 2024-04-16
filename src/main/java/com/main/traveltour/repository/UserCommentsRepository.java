package com.main.traveltour.repository;


import com.main.traveltour.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserCommentsRepository extends JpaRepository<UserComments, Integer> {
    Page<UserComments> findAllByServiceId(String serviceId, Pageable pageable);

    Page<UserComments> findAllByDateCreated(Timestamp dateCreated, Pageable pageable);

    Page<UserComments> findAllByStarAndServiceId(Integer start, String serviceId, Pageable pageable);

    Optional<UserComments> findByOrderId(String orderId);

    List<UserComments> findAllByServiceId(String serviceId);
}
