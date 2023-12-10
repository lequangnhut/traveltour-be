package com.main.traveltour.repository;

import com.main.traveltour.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Users findUserByEmail(String email);

    Users findByPhone(String phone);

    Users findByToken(String token);
}