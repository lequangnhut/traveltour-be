package com.main.traveltour.utils;


import org.modelmapper.ModelMapper;

public class EntityDtoUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <E, D> E convertToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }
}
