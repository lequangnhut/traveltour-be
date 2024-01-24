package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Agencies;

public interface AgenciesService {

    Agencies findByUserId(int userId);

    Agencies findByAgencyId(int agencyId);

    Agencies findByPhone(String phone);

    Agencies findByTaxId(String taxId);

    Agencies save(Agencies agencies);
}
