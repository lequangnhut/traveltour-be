package com.main.traveltour.service.impl;

import com.main.traveltour.entity.Roles;
import com.main.traveltour.repository.RolesRepository;
import com.main.traveltour.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    RolesRepository rolesRepository;

    @Override
    public List<Roles> findAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles findByNameRole(String nameRole) {
        return rolesRepository.findByNameRole(nameRole);
    }

    @Override
    public Roles save(Roles role) {
        return rolesRepository.save(role);
    }
}
