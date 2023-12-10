package com.main.traveltour.repository;

import com.main.traveltour.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Roles findByNameRole(String nameRole);
}