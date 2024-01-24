package com.main.traveltour.service.admin;


import com.main.traveltour.entity.Tours;

import java.util.List;

public interface ToursServiceAD {
    List<Tours> findByTourTypeId(int typeId);
}
