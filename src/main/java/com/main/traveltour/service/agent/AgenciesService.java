package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Agencies;

import java.util.Optional;

public interface AgenciesService {

    Agencies findByUserId(int userId);

    Agencies findByAgencyId(int agencyId);

    Agencies findByPhone(String phone);

    Agencies findByTaxId(String taxId);

    Agencies save(Agencies agencies);

    Optional<Agencies> findById(Integer id);
}
