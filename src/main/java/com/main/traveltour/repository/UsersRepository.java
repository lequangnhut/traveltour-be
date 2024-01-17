package com.main.traveltour.repository;

import com.main.traveltour.entity.Users;
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

    Users findByToken(String token);

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_ADMIN' " +
            "OR r.nameRole LIKE 'ROLE_STAFF' " +
            "OR r.nameRole LIKE 'ROLE_GUIDE'" +
            "ORDER BY CASE WHEN 'ROLE_ADMIN' IN (SELECT role.nameRole FROM u.roles role) THEN 0 ELSE 1 END, u.dateCreated DESC")
    List<Users> findDecentralizationStaffByActiveIsTrue();

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_AGENT_TRANSPORT' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_HOTEL' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_PLACE'" +
            "ORDER BY CASE WHEN 'ROLE_AGENT_HOTEL' IN (SELECT role.nameRole FROM u.roles role) THEN 0 ELSE 1 END, u.dateCreated DESC")
    List<Users> findDecentralizationAgentByActiveIsTrue();

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_ADMIN' " +
            "OR r.nameRole LIKE 'ROLE_STAFF' " +
            "OR r.nameRole LIKE 'ROLE_GUIDE' " +
            "ORDER BY u.dateCreated DESC")
    List<Users> findAllAccountStaffOrderByDateCreatedDESC();

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_AGENT_HOTEL' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_TRANSPORT' " +
            "OR r.nameRole LIKE 'ROLE_AGENT_PLACE' " +
            "ORDER BY u.dateCreated DESC")
    List<Users> findAllAccountAgentOrderByDateCreatedDESC();
}