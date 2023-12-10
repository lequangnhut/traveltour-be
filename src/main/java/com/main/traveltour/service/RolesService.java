package com.main.traveltour.service;

import com.main.traveltour.entity.Roles;

import java.util.List;

public interface RolesService {

    List<Roles> findAllRoles();

    Roles findByNameRole(String nameRole);

    Roles save(Roles role);
}
