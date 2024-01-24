package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Tours;
import com.main.traveltour.entity.VisitLocationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitLocationTypesServiceAD {

    List<VisitLocationTypes> findAll();

    Page<VisitLocationTypes> findAll(Pageable pageable);

    Page<VisitLocationTypes> findAllWithSearch(String searchTerm, Pageable pageable);

    VisitLocationTypes findByVisitLocationTypeName(String name);

    VisitLocationTypes findById(int id);

    VisitLocationTypes save(VisitLocationTypes visitLocationTypes);

    VisitLocationTypes delete(int id);


}
