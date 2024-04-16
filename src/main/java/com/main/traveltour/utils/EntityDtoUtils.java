package com.main.traveltour.utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityDtoUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <E, D> E convertToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <T, U> List<U> convertToDtoList(List<T> sourceList, Class<U> destinationClass) {
        List<U> destinationList = new ArrayList<>();
        for (T source : sourceList) {
            U destination = convertToDto(source, destinationClass);
            destinationList.add(destination);
        }
        return destinationList;
    }

    public static <T, U> Page<U> convertPage(Page<T> sourcePage, Class<U> destinationClass) {
        List<U> destinationList = sourcePage.getContent().stream()
                .map(source -> convertToDto(source, destinationClass))
                .collect(Collectors.toList());
        return new PageImpl<>(destinationList, sourcePage.getPageable(), sourcePage.getTotalElements());
    }

    public static <T, U> U convertOptionalToDto(Optional<T> optional, Class<U> destinationClass) {
        if (optional.isPresent()) {
            T entity = optional.get();
            return convertToDto(entity, destinationClass);
        } else {
            return null;
        }
    }
}
