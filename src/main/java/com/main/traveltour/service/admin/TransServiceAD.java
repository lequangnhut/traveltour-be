package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Transportations;

import java.util.List;

public interface TransServiceAD {

    List<Transportations> findbyTransTypeId(int typeId);
}
