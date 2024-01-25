package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationImageRepository extends JpaRepository<TransportationImage, Integer> {

    List<TransportationImage> findAllByTransportationId(String transportationId);

    void deleteById(int imgId);
}