package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Agencies;

public interface AgenciesService {

    Agencies findByUserId(int userId);

    Agencies save(Agencies agencies);
}
