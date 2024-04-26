package com.main.traveltour.repository;

import com.main.traveltour.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT DISTINCT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole IN ('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_GUIDE')")
    Page<Users> findDecentralizationStaff(Pageable pageable);

    @Query("SELECT DISTINCT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole IN ('ROLE_AGENT_HOTEL', 'ROLE_AGENT_TRANSPORT', 'ROLE_AGENT_PLACE')")
    Page<Users> findDecentralizationAgent(Pageable pageable);

    @Query("SELECT DISTINCT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole IN ('ROLE_CUSTOMER') AND u.isActive = TRUE")
    Page<Users> findDecentralizationCustomer(Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE (r.nameRole LIKE 'ROLE_ADMIN' OR r.nameRole LIKE 'ROLE_STAFF' OR r.nameRole LIKE 'ROLE_GUIDE') " +
            "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Users> searchAccountStaff(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE (r.nameRole LIKE 'ROLE_AGENT_HOTEL' OR r.nameRole LIKE 'ROLE_AGENT_TRANSPORT' OR r.nameRole LIKE 'ROLE_AGENT_PLACE') " +
            "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Users> searchAccountAgent(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE (r.nameRole LIKE 'ROLE_CUSTOMER') " +
            "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Users> searchAccountCustomer(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole LIKE 'ROLE_GUIDE'")
    List<Users> findUsersByRolesIsGuild();

    @Query("SELECT u.id FROM Users u WHERE u.phone = :phone AND u.id <> :currentUserId")
    Integer findIdByPhoneNumberAndNotCurrentUser(@Param("phone") String phone, @Param("currentUserId") Integer currentUserId);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE u.email = :email AND u.isActive = true AND (r.nameRole LIKE 'ROLE_CUSTOMER')")
    Users findByEmailAndActive(@Param("email") String email);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE u.email = :email AND u.isActive = true AND (r.nameRole IN ('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_AGENT_TRANSPORT', 'ROLE_AGENT_HOTEL', 'ROLE_AGENT_PLACE', 'ROLE_GUIDE'))")
    Users findByEmailAndActiveAdmin(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM Users u " +
            "JOIN u.roles r " +
            "WHERE r.nameRole IN ('ROLE_CUSTOMER')")
    Long countUsers ();

    @Query(value="SELECT COUNT(u.id) " +
            "FROM users u " +
            "JOIN roles_users ru on u.id = ru.user_id " +
            "JOIN roles r on ru.role_id = r.id " +
            "WHERE r.name_role LIKE 'ROLE_CUSTOMER' " +
            "AND MONTH(u.date_created) = MONTH(CURRENT_DATE()) " +
            "AND YEAR(u.date_created) = YEAR(CURRENT_DATE());", nativeQuery = true)
    Integer countUserNow();

    @Query(value="SELECT COUNT(u.id) " +
            "FROM users u " +
            "JOIN roles_users ru on u.id = ru.user_id " +
            "JOIN roles r on ru.role_id = r.id " +
            "WHERE r.name_role LIKE 'ROLE_CUSTOMER' " +
            "AND MONTH(u.date_created) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) " +
            "AND YEAR(u.date_created) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH);", nativeQuery = true)
    Integer countUserMonthAgo();

    @Query(value="SELECT COUNT(u.id) " +
            "FROM users u " +
            "JOIN roles_users ru on u.id = ru.user_id " +
            "JOIN roles r on ru.role_id = r.id " +
            "WHERE r.name_role IN ('ROLE_AGENT_HOTEL','ROLE_AGENT_TRANSPORT','ROLE_AGENT_PLACE') " +
            "AND MONTH(u.date_created) = MONTH(CURRENT_DATE()) " +
            "AND YEAR(u.date_created) = YEAR(CURRENT_DATE());", nativeQuery = true)
    Integer countUserAgentNow();

    @Query(value="SELECT COUNT(u.id) " +
            "FROM users u " +
            "JOIN roles_users ru on u.id = ru.user_id " +
            "JOIN roles r on ru.role_id = r.id " +
            "WHERE r.name_role IN ('ROLE_AGENT_HOTEL','ROLE_AGENT_TRANSPORT','ROLE_AGENT_PLACE') " +
            "AND MONTH(u.date_created) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) " +
            "AND YEAR(u.date_created) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH);", nativeQuery = true)
    Integer countUserAgentMonthAgo();
}