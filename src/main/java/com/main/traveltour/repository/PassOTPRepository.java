package com.main.traveltour.repository;

import com.main.traveltour.entity.PassOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassOTPRepository extends JpaRepository<PassOTP, Integer> {

    PassOTP findByUsersIdAndCodeTokenAndIsActiveIsTrue(int id, String token);

    @Query("SELECT p FROM PassOTP p WHERE p.usersId = :userId AND p.isActive = true")
    List<PassOTP> findActivePassOTPsByUserId(@Param("userId") int userId);

    PassOTP findByCodeToken(String token);

    PassOTP findByCodeTokenAndEmail(String codeOTP, String email);

    List<PassOTP> findByIsActiveIsTrue();
}
