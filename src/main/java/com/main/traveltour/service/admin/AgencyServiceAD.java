package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Agencies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgencyServiceAD {

    Long countAgency();

    Long countAllWaiting();

    Agencies findById(int id);

    Agencies checkPhone(String phone);

    Agencies checkTax(String tax);

    Agencies save(Agencies agencies);

    List<Agencies> findByIdAgencyId(int id);

    Page<Agencies> findAllAgenciesByAccepted(Pageable pageable, Boolean isActive);

    Page<Agencies> findAllAgenciesByAcceptedWithSearch(String searchTerm, Pageable pageable, Boolean isActive);

    Page<Agencies> findAllAgenciesWaitingByIsAccepted(Pageable pageable, Integer isActive);

    Page<Agencies> findAllAgenciesWaitingByIsAcceptedWithSearch(String searchTerm, Pageable pageable, Integer isActive);

    List<Agencies> findFiveAgenciesNewest ();
}
