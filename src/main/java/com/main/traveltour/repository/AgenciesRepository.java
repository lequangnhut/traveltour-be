package com.main.traveltour.repository;

import com.main.traveltour.entity.Agencies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenciesRepository extends JpaRepository<Agencies, Integer> {

    @Query("SELECT COUNT(a) FROM Agencies a")
    Long countAgencies();

    @Query("SELECT COUNT(a) FROM Agencies a WHERE a.isAccepted = 1 and a.isActive = true")
    Long countByIsAcceptedIsOne();

    Agencies findById(int agencyId);

    List<Agencies> findAllById(int id);

    Agencies findByUserId(int userId);

    Agencies findByPhone(String phone);

    Agencies findByTaxId(String taxId);

    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = 2 and a.isActive = :isActive")
    Page<Agencies> findAllAgenciesByIsActiveAD(Pageable pageable, @Param("isActive") Boolean isActive);

    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = 2) and (a.isActive = :isActive) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findAllAgenciesByIsActiveWithSearchAD(String searchTerm, Pageable pageable, @Param("isActive") Boolean isActive);

    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = :isAccepted and a.isActive = true")
    Page<Agencies> findAllAgenciesByIsAccepted(Pageable pageable, @Param("isAccepted") Integer isAccepted);

    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = :isAccepted) and (a.isActive = true) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findAllAgenciesByIsAcceptedWithSearchAD(String searchTerm, Pageable pageable, @Param("isAccepted") Integer isAccepted);
}