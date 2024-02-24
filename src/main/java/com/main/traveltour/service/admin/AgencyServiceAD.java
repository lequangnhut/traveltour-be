package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Agencies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgencyServiceAD {

    Page<Agencies> findAllAccepted(Pageable pageable);

    Page<Agencies> findAllAcceptedButFalse(Pageable pageable);

    Page<Agencies> findAllWaiting(Pageable pageable);

    Page<Agencies> findAllDenied(Pageable pageable);

    Long countAllWaiting();

    Page<Agencies> findAllWithSearch(int isAccepted, String searchTerm, Pageable pageable);

    Page<Agencies> findOneTrueWithSearch(String searchTerm, Pageable pageable);

    Page<Agencies> findTwoTrueWithSearch(String searchTerm, Pageable pageable);

    Page<Agencies> findTwoFalseWithSearch(String searchTerm, Pageable pageable);

    Page<Agencies> findThreeTrueWithSearch(String searchTerm, Pageable pageable);

    Agencies findById(int id);

    Agencies checkPhone(String phone);

    Agencies checkTax(String tax);

    Agencies save(Agencies agencies);

    List<Agencies> findByIdAgencyId(int id);
}
