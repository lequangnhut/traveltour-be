package com.main.traveltour.repository;

import com.main.traveltour.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Users findUsersById(int id);

    Users findUserByEmail(String email);

    Users findByPhone(String phone);

    Users findByCitizenCard(String cardId);

    Users findByToken(String token);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_ADMIN' " +
            "OR r.nameRole LIKE 'ROLE_STAFF' " +
            "OR r.nameRole LIKE 'ROLE_GUIDE'" +
            "ORDER BY u.isActive DESC, u.dateCreated DESC")
    Page<Users> findDecentralizationStaffByActiveIsTrue(Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_AGENT_TRANSPORT' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_HOTEL' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_PLACE'" +
            "ORDER BY u.isActive DESC, u.dateCreated DESC")
    Page<Users> findDecentralizationAgentByActiveIsTrue(Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_ADMIN' " +
            "OR r.nameRole LIKE 'ROLE_STAFF' " +
            "OR r.nameRole LIKE 'ROLE_GUIDE' " +
            "ORDER BY u.isActive DESC, u.dateCreated DESC")
    Page<Users> findAllAccountStaffOrderByDateCreatedDESC(Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_AGENT_HOTEL' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_TRANSPORT' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_PLACE' " +
            "ORDER BY u.isActive DESC, u.dateCreated DESC")
    Page<Users> findAllAccountAgentOrderByDateCreatedDESC(Pageable pageable);
}